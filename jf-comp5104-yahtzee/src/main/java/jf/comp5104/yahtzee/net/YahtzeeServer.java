package jf.comp5104.yahtzee.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
	private ArrayList<TCPConnection> clients;
	protected boolean shutdown;
	protected BlockingQueue<String> messageQueue;

	public YahtzeeServer(String hostname, int port, int poolSize) throws IOException {
		this.hostName = hostname;
		this.portNumber = port;
		this.serverSocket = new ServerSocket(port);
		this.pool = Executors.newFixedThreadPool(poolSize);
		this.messageQueue = new LinkedBlockingQueue<String>();
		this.clients = new ArrayList<TCPConnection>(10);
		shutdown = false;
		System.out.println("Server socket " + hostName + " : " + port);

	}

	public YahtzeeServer(int port, int poolSize) throws IOException {
		this("localhost", port, poolSize);
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

	// YahtzeeServer thread listens for and adds clients.
	@Override
	public void run() {
		int port = getPort();

		System.out.println("Server listening on port " + port);
		pool.execute(new MessageQueueHandler(this));

		try {
			while (shutdown) {
				// listening for connections
				Socket newClient = serverSocket.accept();
				TCPConnection newConnection = new TCPConnection(newClient);
				System.out.println("New connection: " + newConnection.toString());
				clients.add(newConnection);
				// ClientHandler gets input from client, adds to queue
				pool.execute(new ClientHandler(newConnection, this));
			}
		} catch (IOException ex) {
			System.err.println("Unable to start listening");
			System.err.println(ex.toString());
			pool.shutdown();
		}
		shutdown();
	}

	public int getPort() {
		return portNumber;
	}

	void shutdown() {
		shutdownAndAwaitTermination(this.pool);
	}

	// shutdown
	void shutdownAndAwaitTermination(ExecutorService pool) {
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
		shutdown = true;
	}

	public boolean getStayOn() {
		return shutdown;
	}

	// nested MessageQueue class polls the queue and broadcasts to all clients
	class MessageQueueHandler implements Runnable {
		YahtzeeServer server;

		MessageQueueHandler(YahtzeeServer server) {
			this.server = server;
		}

		public void run() {
			System.out.println("Listening for messages");
			while (server.getStayOn()) {
				try {
					if (server.messageQueue.isEmpty()) {
						// for debug
						// System.out.println("No messages");
						Thread.sleep(5000);
					} else {
						// System.out.println("take message from q");
						String msg = server.messageQueue.take();
						// System.out.println(msg);
						broadcast(msg);
					}
				} catch (InterruptedException e) {
					System.err.println("Error in receive Msg?");
				}
			}
		}

		public void broadcast(String str) {
			for (TCPConnection c : clients) {
				// System.out.println("Broadcasting to " + c.getId());
				c.send(str);
			}
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

		public void run() {
			session.send("Welcome to CLI Yahtzee");
			String inputLine;
			while (!shutdown) {
				// System.out.println("Listening for messages on thread " +
				// this.hashCode());
				inputLine = session.receive();
				// send command to server through queue
				// System.out.println("Adding message " + inputLine);
				server.messageQueue.add(inputLine);
				// System.out.println("message added to queue");
				// received exit command
				if ("exit".equalsIgnoreCase(inputLine)) {
					session.send("Bye");
					shutdown = true;
				}
			}
		}
	}

}