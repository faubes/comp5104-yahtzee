package jf.comp5104.yahtzee.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jf.comp5104.yahtzee.AlreadyScoredThereException;
import jf.comp5104.yahtzee.Game;
import jf.comp5104.yahtzee.Game.InputGameState;
import jf.comp5104.yahtzee.NotYourTurnException;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.Yahtzee;
import jf.comp5104.yahtzee.net.PlayerCommand.Command;

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
	private HashMap<Player, TCPConnection> playerSessionMap;
	private HashMap<TCPConnection, Player> sessionPlayerMap;
	protected boolean shutdown;
	protected BlockingQueue<Message> messageQueue;
	private Game g; // maybe want to be able to host multiple games?

	public YahtzeeServer(String hostname, int port, int poolSize) throws IOException {
		// socket and info
		this.hostName = hostname;
		this.portNumber = port;
		this.serverSocket = new ServerSocket(port);
		// thread executor
		this.pool = Executors.newFixedThreadPool(poolSize);
		// thread safe structure
		this.messageQueue = new LinkedBlockingQueue<Message>();
		// list of client handling threads
		this.clientHandlers = new ArrayList<ClientHandler>(10);
		// list of clients
		this.clients = new ArrayList<TCPConnection>(10);
		// thread for processing messageQ
		this.messageQueueHandler = new MessageQueueHandler(this);
		this.shutdown = false;

		this.playerSessionMap = new HashMap<Player, TCPConnection>();
		this.sessionPlayerMap = new HashMap<TCPConnection, Player>();

		System.out.println("Server socket " + hostName + " : " + port);

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
				System.out.println("New connection: " + newConnection.toString());
				// add to list of clients
				clients.add(newConnection);
				// create new player and add to session map
				Player p = new Player();
				playerSessionMap.put(p, newConnection);
				sessionPlayerMap.put(newConnection, p);
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

	public int getPort() {
		return portNumber;
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

	// distinguish output for owned event
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

	// direct from server, no client session attached to message
	public void broadcast(String str) {
		for (TCPConnection c : clients) {
			// System.out.printlnmessageQueue("Broadcasting to " + c.getId());
			c.send(str);
		}

	}

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

	// nested class Message encapsulates client and string message

	class Message {
		private TCPConnection sender;
		private String text;

		Message(TCPConnection c, String s) {
			this.sender = c;
			this.text = s;
		}

		public TCPConnection getSender() {
			return sender;
		}

		public String toString() {
			return text;
		}
	}

	// nested MessageQueue class polls the queue and broadcasts to all clients
	class MessageQueueHandler implements Runnable {

		private volatile boolean shutdown;
		YahtzeeServer server;

		MessageQueueHandler(YahtzeeServer server) {
			this.server = server;
			this.shutdown = false;
		}

		void setShutdown(boolean b) {
			this.shutdown = b;
		}

		public void run() {
			System.out.println("Listening for messages");
			while (!shutdown) {
				try {
					if (server.isQueueEmpty()) {
						// for debug
						// System.out.println("No messages");
						Thread.sleep(500);
					} else {
						// System.out.println("take message from q");
						processMessage(server.getMessage());
						// System.out.println(msg);
					}
				} catch (InterruptedException e) {
					System.err.println("Error in receive Msg?");
				}
			}
		}

		private void processMessage(Message msg) {
			if (msg.text == null) {
				return;
			}
			
			Command cmd = Command.getCommandFromString(msg.text);
			Player p = sessionPlayerMap.get(msg.getSender());
			
			try {
				if (hasGameStarted() && g.isCurrentPlayer(p) && g.isAwaitingIndexSet()) {
					int[] rerollIndex = cmd.getRerollIndicies()
							.stream()
							.mapToInt(Integer::intValue)
							.toArray();
					g.reroll(p, rerollIndex);
					server.broadcast(p.getName() + " rerolls " + Arrays.toString(rerollIndex));
					server.broadcast(g.getCurrentPlayer().getRoll().toString());
					g.setInputState(InputGameState.NEEDCOMMAND);
					sendToCurrentPlayer(g.promptPlayer(p));
					return;
				}
				if (hasGameStarted() && g.isCurrentPlayer(p) && g.isAwaitingCategory()) {
					int[] rerollIndex = cmd.getRerollIndicies()
							.stream()
							.mapToInt(Integer::intValue)
							.toArray();
					g.score(p, rerollIndex[0]);
					server.broadcast(p.getName() + " scores in category " + rerollIndex[0]);
					g.setInputState(InputGameState.NEEDCOMMAND);
					server.broadcast(g.toString());
					sendToCurrentPlayer(g.promptPlayer(g.getCurrentPlayer()));
					return;
				}
				
				switch (cmd) {
				case SAY:
					server.broadcast(new Message(msg.getSender(), msg.text.substring(4)));
					break;
				case START:
					if (hasGameStarted()) {
						respond(msg, "Game has already started.");
						break;
					} else {
						g = new Game(playerSessionMap.keySet()).start();
						server.broadcast("Game has begun!");
						server.broadcast(g.toString());
						sendToCurrentPlayer(g.promptPlayer(g.getCurrentPlayer()));
					}
					break;
				case ENTER:
					if (hasGameStarted() && g.isCurrentPlayer(p) && g.isFirstRoll()) {
						g.roll(p);
						server.broadcast(p.getName() + " rolls!");
						server.broadcast(g.getCurrentPlayer().getRoll().toString());
						sendToCurrentPlayer(g.promptPlayer(p));
					}
					break;
				case REROLLALL:
					if (hasGameStarted() && g.isCurrentPlayer(p) && !g.isFirstRoll()) {
						g.reroll(p, 1,2,3,4,5);
						server.broadcast(p.getName() + " rerolls everything!");
						server.broadcast(g.getCurrentPlayer().getRoll().toString());
						sendToCurrentPlayer(g.promptPlayer(p));
					}
					break;
				case REROLLSOME:
					if (hasGameStarted() && g.isCurrentPlayer(p) && !g.isFirstRoll()) {
						g.setInputState(InputGameState.NEEDINDEXSET);
						sendToCurrentPlayer(g.promptPlayer(p));
					}
					break;
				case SCORE:
					if (hasGameStarted() && g.isCurrentPlayer(p) && !g.isFirstRoll()) {
						g.setInputState(InputGameState.NEEDCATEGORY);
						sendToCurrentPlayer(g.promptPlayer(p));
					}
					break;
				default:
					//do nothing
					break;
				}
			} catch (NotYourTurnException e) {
				respond(msg, "It's not your turn.");
			}
			catch (AlreadyScoredThereException e) {
				respond(msg, "You've already scored in that category.");
				g.setInputState(InputGameState.NEEDCOMMAND);
				sendToCurrentPlayer(g.promptPlayer(p));
			}
			catch (IndexOutOfBoundsException e) {
				respond(msg, "That is not a valid category. Choose 1-13.");
				sendToCurrentPlayer(g.promptPlayer(p));
			}
		}

		private boolean hasGameStarted() {
			return g != null && g.hasStarted();
		}

		// respond to a message from sender with string
		private void respond(Message msg, String string) {
			msg.getSender().send(string);
		}

		private void sendToCurrentPlayer(String string) {
			Player cur = g.getCurrentPlayer();
			playerSessionMap.get(cur).send(string);
		}
	}

	class ClientHandler implements Runnable {
		private final TCPConnection session;
		private final YahtzeeServer server;
		private volatile boolean shutdown;

		ClientHandler(TCPConnection session, YahtzeeServer server) {
			this.session = session;
			this.server = server;
			this.shutdown = false;
		}

		void setShutdown(boolean b) {
			shutdown = b;
		}

		public void run() {
			session.send("Welcome to CLI Yahtzee");
			String inputLine;
			while (!shutdown) {
				// System.out.println("Listening for messages on thread " +
				// this.hashCode());
				inputLine = session.receive();
				// send command to server through queue
				// System.out.println("Adding message " + inputLine);
				server.addMessage(session, inputLine);
				// System.out.println("message added to queue");
				// received exit command
				if (shutdown) {
					session.send("Server shutdown");
				}
				if ("exit".equalsIgnoreCase(inputLine)) {
					// need to remove player
					session.send("Bye");
					shutdown = true;
				}
			}
		}
	}

}