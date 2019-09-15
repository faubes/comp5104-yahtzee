package jf.comp5104.yahtzee.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jf.comp5104.yahtzee.Player;

// sources and tutorials for networking components
// docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
// docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
// cs.lmu.edu/~ray/notes/javanetexamples/#tictactoe
// add logging eventually?

public class YahtzeeServer implements Runnable, AbstractServer {
	public static final String DEFAULT_PORT = "3333";
	public static final String DEFAULT_HOST = "localhost";

	private int portNumber;
	private String hostName;
	// private Socket socket;

	private final ServerSocket serverSocket;
	private final ExecutorService pool;
	private ArrayList<Player> players;
	private ArrayList<AbstractSession> clients;
	private boolean stayOn;
	private boolean isOn;

	public YahtzeeServer(int port, int poolSize) throws IOException {
		portNumber = port;
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);
		System.out.println("Created server socket on port " + port);
		ArrayList<AbstractSession> clients = new ArrayList<AbstractSession>(10);
		stayOn = true;
	}

	@Override
	public void run() {
		int port = getPort();

		System.out.println("Server listening on port " + port);
		isOn = true;
		try {
			while (stayOn) {
				// listening for connections
				Socket newClient = serverSocket.accept();
				AbstractSession newConnection = new TCPConnection(newClient);
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

	@Override
	public void handleMsgFromClient() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMsgFromUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean serverStarted() {
		return isOn;
	}

	@Override
	public boolean serverStopped() {
		return !isOn;
	}

	@Override
	public boolean start() {
		stayOn = true;
		return false;
	}

	@Override
	public boolean stop() {
		stayOn = false;
		return stayOn;
	}
}

// handler

class ClientHandler implements Runnable {
	private final AbstractSession session;
	private final AbstractServer server;
	boolean stayOn;

	ClientHandler(AbstractSession session, AbstractServer server) {
		this.session = session;
		this.server = server;
		boolean stayOn = true;
	}

	public void run() {
		session.send("Welcome to CLI Yahtzee");
		for (;;) {
			String inputLine, outputLine;
			while (stayOn) {

				inputLine = (String) session.receive();
				session.send(inputLine);
				// received exit command
				if ("exit".equalsIgnoreCase(inputLine)) {
					session.send("Bye");
					stayOn = false;
					break;
				}
			}

		}
	}
}
