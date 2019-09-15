package jf.comp5104.yahtzee;

import java.net.*;

import jf.comp5104.yahtzee.net.AbstractSession;
import jf.comp5104.yahtzee.net.TCPConnection;

import java.io.*;

public class Player {
	String username;
	AbstractSession session;

	public Player(String username) {
		this.username = username;
	}
	
	public Player(String username, AbstractSession s) {
		this.username = username;
		this.session = s;
	}
	
	public Player(String username, String hostname, int port) throws IOException {
		this(username, new TCPConnection(hostname, port));
	}

	public String getName() {
		return username;
	}

}
