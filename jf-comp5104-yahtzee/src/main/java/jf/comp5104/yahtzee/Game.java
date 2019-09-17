package jf.comp5104.yahtzee;

import java.util.ArrayDeque;
import java.util.Deque;


import jf.comp5104.yahtzee.net.YahtzeeServer;

public class Game {

	YahtzeeServer server;
	Deque<Player> players;
	private boolean started;
	private boolean finished;
	private int round;
	private int turnCount;
	
	Game (String name, YahtzeeServer server) {
		this.server = server;
		this.players = new ArrayDeque<Player>(5);
		this.round = 0;		
		this.turnCount = 0;
		this.started = false;
		this.finished = false;
	}
	
	Player getCurrentPlayer() {
		return players.peek();
	}
	
	void endTurn() {
		players.addLast(players.pollFirst()); // move player to end of the deque
		turnCount++;
		if (turnCount % players.size() == 0) 
			round++;
	}
	
	void display() {
		for (Player p : players) {
			server.broadcast(p.getScoresheet().toString());
		}
	}

	public void addPlayer(Player p) {
		if (started) {
			System.err.println("Cannot add player to game in progress!");
			
		} else {
			players.addLast(p);
		}
	}

	public void removePlayer(Player p) {
		players.remove(p);
	}
	
	public int getRound() {
		return round;
	}
}
