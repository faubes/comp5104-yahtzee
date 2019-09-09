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
	}

	public String getName() {
		return username;
	}

}
