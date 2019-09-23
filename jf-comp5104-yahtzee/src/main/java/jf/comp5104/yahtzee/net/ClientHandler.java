package jf.comp5104.yahtzee.net;

public class ClientHandler implements Runnable {
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
