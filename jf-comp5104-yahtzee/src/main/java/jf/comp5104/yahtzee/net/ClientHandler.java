package jf.comp5104.yahtzee.net;

import jf.comp5104.yahtzee.net.PlayerCommand.Command;

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
		session.send(Command.getCommands());
		String inputLine;
		while (!shutdown) {
			// System.out.println("Listening for messages on thread " +
			// this.hashCode());
			inputLine = session.receive();
			if (inputLine == null) {
				// lost connection?
				System.out.println("Server got null: assuming connection lost and close handler");
				server.disconnect(session);
				shutdown = true;
			}
			// send command to server through queue
			server.addMessage(session, inputLine);

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
