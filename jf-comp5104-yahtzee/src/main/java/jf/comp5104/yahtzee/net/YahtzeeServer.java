package jf.comp5104.yahtzee.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jf.comp5104.yahtzee.Player;

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
	// private Socket socket;

	private final ServerSocket serverSocket;
	private final ExecutorService pool;
	private ArrayList<Player> players;
	private ArrayList<TCPConnection> clients;
	protected boolean stayOn;
	protected boolean isOn;
	protected BlockingQueue<String> messageQueue;

	public YahtzeeServer(int port, int poolSize) throws IOException {
		portNumber = port;
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);
		messageQueue = new LinkedBlockingQueue<String>();
		System.out.println("Created server socket on port " + port);
		clients = new ArrayList<TCPConnection>(10);
		stayOn = true;

	}

	@Override
	public void run() {
		int port = getPort();

		System.out.println("Server listening on port " + port);
		isOn = true;
		try {
			while (stayOn) {
				if (!clients.isEmpty()) {
					pool.execute(new Listener(this));
				}
				// listening for connections
				Socket newClient = serverSocket.accept();
				TCPConnection newConnection = new TCPConnection(newClient);
				System.out.println("New connection: " + newConnection.toString());
				clients.add(newConnection);
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
		stayOn = false;
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
		return isOn;
	}

	public boolean serverStopped() {
		return !isOn;
	}

	public boolean start() {
		stayOn = true;
		return false;
	}

	public boolean stop() {
		stayOn = false;
		return stayOn;
	}

	public boolean getStayOn() {
		return stayOn;
	}

	// nested listener class
	// polls the messageQueue and broadcasts to all clients
	class Listener implements Runnable {
		YahtzeeServer server;

		Listener(YahtzeeServer server) {
			this.server = server;
		}

		public void run() {
			System.out.println("Listening for messages");
			while (server.getStayOn()) {
				try {
					if (server.messageQueue.isEmpty()) {
						System.out.println("No messages");
						Thread.sleep(5000);
					} else {
						System.out.println("take message from q");
						String msg = server.messageQueue.take();
						System.out.println(msg);
						broadcast(msg);
					}
				} catch (InterruptedException e) {
					System.err.println("Error in receive Msg?");
				}
			}
		}

		public void broadcast(String str) {
			for (TCPConnection c : clients) {
				System.out.println("Broadcasting to " + c.getId());
				c.send(str);
			}
		}

	}

	class ClientHandler implements Runnable {
		private final TCPConnection session;
		private final YahtzeeServer server;
		boolean stayOn;

		ClientHandler(TCPConnection session, YahtzeeServer server) {
			this.session = session;
			this.server = server;
			stayOn = true;
		}

		public void run() {
			session.send("Welcome to CLI Yahtzee");
			String inputLine;
			while (stayOn) {
				System.out.println("Listening for messages on thread " + this.hashCode());
				inputLine = session.receive();
				// process commands
				System.out.println("Adding message " + inputLine);
				server.messageQueue.add(inputLine);
				System.out.println("message added to queue");
				// for now broadcast?
				// received exit command
				if ("exit".equalsIgnoreCase(inputLine)) {
					session.send("Bye");
					stayOn = false;
				}
			}
		}
	}

}

// handler