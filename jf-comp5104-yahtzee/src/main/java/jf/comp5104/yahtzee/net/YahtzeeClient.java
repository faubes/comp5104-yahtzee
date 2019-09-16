package jf.comp5104.yahtzee.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;

public class YahtzeeClient implements Runnable {
	int portNumber;
	String hostName;
	TCPConnection session;
	BufferedReader stdIn;
	boolean stayOn;
	ServerHandler serverHandler;

	public YahtzeeClient(String host, int port) {
		portNumber = port;
		hostName = host;
		try {
			session = new TCPConnection(hostName, portNumber);
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			stayOn = true;
			System.out.println("Connected to server " + hostName + " on port " + portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverHandler = new ServerHandler(session, this);
		new Thread(serverHandler).start();
		new Thread(this).start();
	}

	@Override
	public void run() {
		String userInput;
		while (stayOn) {
			//System.out.println("Awaiting user input");
			try {
				userInput = stdIn.readLine();
				// System.out.println("Client input: " + userInput);
				if (StringUtils.isNotBlank(userInput)) {
					session.send(userInput);
				}
				if (StringUtils.startsWith(userInput.toLowerCase(), "quit")) {
					stayOn = false;
				}
			} catch (IOException e) {
				stayOn = false;
				e.printStackTrace();
			}
		}
		System.out.println("Disconnected.");
	}

	class ServerHandler implements Runnable {

		TCPConnection session;
		YahtzeeClient client;

		public ServerHandler(TCPConnection session, YahtzeeClient client) {
			this.session = session;
			this.client = client;
		}

		public void run() {
			String fromServer;
			while (client.stayOn) {
				// System.out.println("Listening for server");
				fromServer = session.receive();
				System.out.println("Server: " + fromServer);

				if ("Bye".equalsIgnoreCase(fromServer)) {
					break;
				}

			}
		}
	}

}
