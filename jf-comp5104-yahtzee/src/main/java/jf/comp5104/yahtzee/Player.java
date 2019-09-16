package jf.comp5104.yahtzee;

import java.net.*;

import jf.comp5104.yahtzee.net.AbstractSession;
import jf.comp5104.yahtzee.net.TCPConnection;

import java.io.*;

public class Player {
	String username;

	public Player(String username) {
		this.username = username;
	}
	
	public String getName() {
		return username;
	}

}
