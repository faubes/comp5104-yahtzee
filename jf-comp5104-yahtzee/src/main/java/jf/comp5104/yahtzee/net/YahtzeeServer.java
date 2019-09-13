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

	public YahtzeeServer(int port, int poolSize) throws IOException {
		portNumber = port;
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);
		System.out.println("Created server socket on port " + port);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int port = getPort();

		System.out.println("Server listening on port " + port);
		try {
			for (;;) {
				pool.execute(new Handler(serverSocket.accept()));
			}
		} catch (IOException ex) {
			pool.shutdown();
		}
	}

	public int getPort() {
		return portNumber;
	}

	void shutdown() {
		shutdownAndAwaitTermination(this.pool);
	}

	// shutdown
	void shutdownAndAwaitTermination(ExecutorService pool) {
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
}

// handler

class Handler implements Runnable {
	private final Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	Handler(Socket socket) {
		this.socket = socket;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		out.println("Welcome to CLI Yahtzee");
		for (;;) {
			String inputLine, outputLine;
			try {
				while ((inputLine = in.readLine()) != null) {
					out.println(inputLine);
					// received exit command
					if ("exit".equalsIgnoreCase(inputLine)) {
						out.println("Bye");
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
