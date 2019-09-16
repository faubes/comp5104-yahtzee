package jf.comp5104.yahtzee;

import java.net.*;

import jf.comp5104.yahtzee.net.AbstractSession;
import jf.comp5104.yahtzee.net.TCPConnection;

import java.io.*;

public class Player {
	String username;
	Scoresheet scoresheet;
	TCPConnection session; // should probably know own connection, right?
	private boolean connected;

	public Player(String username) {
		this.username = username;
		this.scoresheet = new Scoresheet(username);
	}
	
	public Player() {
		this("New Player");
	}

	public String getName() {
		return username;
	}

	public int getScore() {
		return scoresheet.getTotal();
	}

	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(boolean b) {
		connected = b;
	}
}
