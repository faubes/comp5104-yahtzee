package jf.comp5104.yahtzee.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class YahtzeeServer {
	public static final String DEFAULT_PORT = "3333";
	public static final String DEFAULT_HOST = "localhost";

	private int portNumber;
	private String hostName;
	private ArrayList<Player> players;
	private Socket socket;

	public YahtzeeServer(int port) {
		portNumber = port;
	}

	public int getPort() {
		return portNumber;
	}

	public void start() {
		int port = getPort();
		// sources and tutorials for networking components
		// docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
		// docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
		// cs.lmu.edu/~ray/notes/javanetexamples/#tictactoe
		// add logging eventually?

		System.out.println("Server listening on port " + port);

		try (ServerSocket serverSocket = new ServerSocket(port);
				Socket clientSocket = serverSocket.accept(); // need to add thread here
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

			System.out.println("Created server socket on port " + port);

			out.println("Welcome to CLI Yahtzee");

			String inputLine, outputLine;

			while ((inputLine = in.readLine()) != null) {
				out.println(inputLine);
				if ("exit".equalsIgnoreCase(inputLine)) {
					out.println("Bye");
					break;
				}
			}
		} catch (IOException e) {
			// server socket threw an io exception
			System.out.println(e.toString());
			return;
		}
	}
}
