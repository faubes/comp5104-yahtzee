package jf.comp5104.yahtzee.net;

import java.net.*;
import java.io.*;

public class Player {
	Socket socket;
	String username;
	PrintWriter out;
	BufferedReader in;

	
	public Player(String username) {
		this.username = username;
	
		/*
		 * Socket clientSocket = serverSocket.accept(); // need to add thread here
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		 */
	}

	public String getName() {
		return username;
	}

}
