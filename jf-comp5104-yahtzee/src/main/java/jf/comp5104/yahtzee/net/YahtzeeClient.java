package jf.comp5104.yahtzee.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

public class YahtzeeClient implements Runnable {
	int portNumber;
	String hostName;
	TCPConnection session;
	BufferedReader stdIn;
	private volatile boolean shutdown;
	ServerHandler serverHandler;

	private Thread serverThread;
	private Thread IOThread;

	public YahtzeeClient(String host, int port) {
		portNumber = port;
		hostName = host;
		try {
			session = new TCPConnection(hostName, portNumber);
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			shutdown = false;
			System.out.println("Connected to server " + hostName + " on port " + portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		System.out.println("Starting up client threads");
		serverHandler = new ServerHandler(session, this);
		serverThread = new Thread(serverHandler);
		IOThread = new Thread(this);
		serverThread.start();
		IOThread.start();
	}

	public void stop() {
		System.out.println("Turning off client threads");
		serverHandler.stop();
		shutdown = true;
	}

	@Override
	public void run() {
		String userInput;
		while (!shutdown) {
			if (Thread.currentThread().isInterrupted()) {
				// shutdown instruction?
				stop();
				break;
			}
			// System.out.println("Awaiting user input");
			try {
				userInput = stdIn.readLine();
				// System.out.println("Client input: " + userInput);
				session.send(userInput);
				if (StringUtils.startsWith(userInput.toLowerCase(), "quit")) {
					shutdown = true;
				}
			} catch (IOException e) {
				shutdown = false;
				e.printStackTrace();
			}
		}
		System.out.println("Disconnected.");
	}

	class ServerHandler implements Runnable {

		TCPConnection session;
		YahtzeeClient client;
		private volatile boolean shutdown;

		public ServerHandler(TCPConnection session, YahtzeeClient client) {
			this.session = session;
			this.client = client;
		}

		public void stop() {
			this.shutdown = true;
		}

		public void run() {
			String fromServer;
			while (!shutdown) {
				if (Thread.currentThread().isInterrupted()) {
					// shutdown instruction?
					stop();
					break;
				}
				// System.out.println("Listening for server");
				fromServer = session.receive();
				if (fromServer == null) {
					System.out.println("Lost connection to server.");
					shutdown=true;
					break;
				}
				System.out.println(fromServer);

				if ("Bye".equalsIgnoreCase(fromServer)) {
					client.stop();
					break;
				}

			}
		}
	}

}
