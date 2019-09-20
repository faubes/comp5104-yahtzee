package jf.comp5104.yahtzee;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;


public class Game {

	Deque<Player> players;
	private boolean started;
	private boolean finished;
	private int round;
	private int turnCount;
	
	Game (String name) {
		this.players = new ArrayDeque<Player>(5);
		this.round = 0;		
		this.turnCount = 0;
		this.started = false;
		this.finished = false;
	}
	
	Game () {
		this("Default Game");
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

	public int getNumberOfPlayers() {
		return players.size();
	}

	public ArrayList<Player> getPlayersSortedByScore() {
		ArrayList<Player> playersSortedByScore = new ArrayList<Player>(players);
		playersSortedByScore.sort(Player::compareTo);;
		return playersSortedByScore;
	}
	public Player getWinner() {
		// what about a tie?
		return getPlayersSortedByScore().get(1);
	}

	public void start() {
		this.started = true;
		this.finished = false;
		this.round = 1;
		
	}

	public boolean hasStarted() {
		return this.started;
	}

	public boolean hasEnded() {
		return this.finished;
	}

	public void roll(Player p) {
		p.roll();
	}

	public void score(Player p, int i) {
		p.score(i);
		endTurn();
	}

	public void quit() {
		this.started = false;
		this.finished = true;
	}

	public String getScoresheets() {
		StringBuilder sb = new StringBuilder();
		for (Player p : players) {
			sb.append(p.getScoresheet().toString());
			sb.append(Yahtzee.EOL);
		}
		return sb.toString();
	}

}
