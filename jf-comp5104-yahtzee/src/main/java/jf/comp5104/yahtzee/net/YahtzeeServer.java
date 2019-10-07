package jf.comp5104.yahtzee.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.Yahtzee;

// sources and tutorials for networking components
// docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
// docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
// cs.lmu.edu/~ray/notes/javanetexamples/#tictactoe
// add logging eventually?

public class YahtzeeServer implements Runnable {
	public static final String DEFAULT_PORT = "3333";
	public static final String DEFAULT_HOST = "localhost";

	private int portNumber;
	private String hostName;
	private final ServerSocket serverSocket;
	private final ExecutorService pool;
	private MessageQueueHandler messageQueueHandler;
	private ArrayList<TCPConnection> clients;
	private ArrayList<ClientHandler> clientHandlers;
	HashMap<Player, TCPConnection> playerSessionMap; // package visibility for MessageQueueHandler
	HashMap<TCPConnection, Player> sessionPlayerMap; // package visibility for MessageQueueHandler
	private boolean shutdown;
	private BlockingQueue<Message> messageQueue;

	public YahtzeeServer(String hostname, int port, int poolSize) throws IOException {
		// socket and info
		this.hostName = hostname;
		this.portNumber = port;
		this.serverSocket = new ServerSocket(port);
		// thread executor
		ThreadFactory namedThreadFactory =
				new ThreadFactoryBuilder().setNameFormat("client-thread-%d").build();

		this.pool = Executors.newFixedThreadPool(poolSize, namedThreadFactory);
		// thread safe structure
		this.messageQueue = new LinkedBlockingQueue<Message>();
		
		this.clientHandlers = new ArrayList<>();
		// list of clients
		this.clients = new ArrayList<TCPConnection>(10);
		// thread for processing messageQ
		this.messageQueueHandler = new MessageQueueHandler(this);
		this.shutdown = false;
		
		this.playerSessionMap = new HashMap<Player, TCPConnection>();
		this.sessionPlayerMap = new HashMap<TCPConnection, Player>();
		// System.out.println("Server socket " + hostName + " : " + port);

	}

	public YahtzeeServer(int port, int poolSize) throws IOException {
		this("localhost", port, poolSize);
	}

	// YahtzeeServer is main thread which:
	// creates messageHandlerThread
	// listens for connections and adds clientHandlerThreads
	// gracefully tears down afterwards
	@Override
	public void run() {
		int port = getPort();

		System.out.println("Server listening on port " + port);
		// start message handling service
		pool.execute(messageQueueHandler);

		try {
			while (!shutdown) {
				// listening for connection
				Socket newClient = serverSocket.accept();
				// connection acquired
				TCPConnection newConnection = new TCPConnection(newClient);
				System.out.println("New connection: " + newConnection.getINetAddress().toString());
				// add to list of clients
				clients.add(newConnection);
				addPlayer(newConnection);
				// create new player and add to session map
				// ClientHandler gets input from client, adds to messageQ
				ClientHandler clientHandler = new ClientHandler(newConnection, this);
				clientHandlers.add(clientHandler);
				pool.execute(clientHandler);
			}
		} catch (IOException ex) {
			System.err.println("Listening loop interrupted");
			System.err.println(ex.toString());
			pool.shutdown();
		}
		shutdown();
	}

	public void addPlayer(TCPConnection connection) {
		Player p = new Player();
		playerSessionMap.put(p, connection);
		sessionPlayerMap.put(connection, p);
	}
	
	public void disconnect(TCPConnection connection) {
		Player p = sessionPlayerMap.get(connection);
		
		messageQueueHandler.removePlayer(p);
		messageQueueHandler.stopGame();
		playerSessionMap.remove(p);
		sessionPlayerMap.remove(connection);
		for (ClientHandler h : clientHandlers) {
			if (h.getClientId() == connection.getId()) {
				h.setShutdown(true);
			}
		}
		clientHandlers.stream().filter(ch -> ch.getClientId() != connection.getId());
		clients.remove(connection);
	}
	
	public int getPort() {
		return portNumber;
	}

	public boolean serverStarted() {
		return !shutdown;
	}

	public boolean serverStopped() {
		return shutdown;
	}

	public void start() {
		shutdown = false;
	}

	public void stop() {
		shutdown();
	}

	// broadcast when a player says something
	public void broadcast(Message msg) {
		for (TCPConnection c : clients) {
			// System.out.println("Broadcasting to " + c.getId());
			if (c == msg.getSender()) {
				c.send("You: " + msg.toString());
			} else {
				c.send(sessionPlayerMap.get(msg.getSender()).getName() + ": " + msg.toString());
			}
		}
	}

	// transmit to all except sender
	public void sendToAllExceptSender(Message msg, String string) {
		for (TCPConnection c : clients) {
			if (c != msg.getSender()) {
				c.send(string);
			}
		}
	}

	public void sendToAllExceptPlayer(Player p, String string) {		
		for (TCPConnection c : clients) {
			if (sessionPlayerMap.get(c) != p) {
				c.send(string);
			}
		}
	}
	
	// direct from server, no client session attached to message
	public void broadcast(String str) {
		for (TCPConnection c : clients) {
			// System.out.printlnmessageQueue("Broadcasting to " + c.getId());
			c.send(str);
		}
	}

	// respond to a message from sender with string
	void respond(Message msg, String string) {
		msg.getSender().send(string);
	}
	
	// send to a particular player using playerSessionMap
	void sendToPlayer(Player p, String string) {
		playerSessionMap.get(p).send(string);
	}
	
	// No messages
	public boolean isQueueEmpty() {
		return messageQueue.isEmpty();
	}

	public Message getMessage() throws InterruptedException {
		if (!isQueueEmpty()) {
			return messageQueue.take();
		}
		return null;
	}

	public void addMessage(TCPConnection c, String str) {
		messageQueue.add(new Message(c, str));
	}

	private void shutdown() {
		shutdown = true;
		messageQueueHandler.setShutdown(true);
		for (ClientHandler c : clientHandlers) {
			c.setShutdown(true);
		}
		shutdownAndAwaitTermination(this.pool);
	}

	// shutdown
	private void shutdownAndAwaitTermination(ExecutorService pool) {
		shutdown = true;
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hostname: ");
		sb.append(hostName);
		sb.append(Yahtzee.EOL);
		sb.append("Port number: ");
		sb.append(portNumber);
		sb.append(Yahtzee.EOL);
		sb.append("Server Socket info: ");
		sb.append(serverSocket.toString());
		sb.append(Yahtzee.EOL);
		sb.append("Connected clients: ");
		sb.append(Yahtzee.EOL);
		int i = 0;
		for (TCPConnection c : clients) {
			sb.append("Client ");
			sb.append(++i);
			sb.append(c.toString());
			sb.append(Yahtzee.EOL);
		}
		return sb.toString();
	}


}