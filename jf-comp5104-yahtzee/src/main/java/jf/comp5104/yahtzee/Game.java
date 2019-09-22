package jf.comp5104.yahtzee;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

public class Game {

	String gameName;
	Deque<Player> players;
	private boolean started;
	private boolean finished;
	private boolean awaitingIndexSet;
	private int round;
	private int turnCount;
	private int rollCount;

	public Game(String name) {
		this.gameName = name;
		this.players = new ArrayDeque<Player>(5);
		this.round = 0;
		this.turnCount = 0;
		this.started = false;
		this.finished = false;
		this.rollCount = 0;
		this.awaitingIndexSet = false;
	}

	public Game() {
		this("Default Game");
	}

	public Game(Set<Player> keySet) {
		this();
		players.addAll(keySet);
	}

	public Player getCurrentPlayer() {
		return players.peek();
	}
	
	void endTurn() {
		players.addLast(players.pollFirst()); // move player to end of the deque
		rollCount = 0; // reset reroll counter
		turnCount++;
		if (turnCount % players.size() == 0) {
			round++;
		}
		if (round > 13) {
			stop();
		}
	}

	public void setName(String name) {
		this.gameName = name;
	}

	public String getName() {
		return this.gameName;
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
		playersSortedByScore.sort(Player::compareTo);
		return playersSortedByScore;
	}

	public Player getWinner() {
		// what about a tie?
		return getPlayersSortedByScore().get(1);
	}

	public Game start() {
		this.started = true;
		this.finished = false;
		this.round = 1;
		return this;
	}

	public void stop() {
		this.started = false;
		this.finished = true;
		this.round = 0;
	}

	public boolean hasStarted() {
		return this.started;
	}

	public boolean hasEnded() {
		return this.finished;
	}

	public void setAwaitingIndexSet(boolean b) {
 		this.awaitingIndexSet = b;
	}
	
	public boolean isAwaitingIndexSet() {
		return this.awaitingIndexSet;
	}
	public boolean isCurrentPlayer(Player p) throws NotYourTurnException {
		if (p != getCurrentPlayer()) {
			throw new NotYourTurnException();
		}
		return true;
	}

	public boolean isFirstRoll() {
		return rollCount == 0;
	}
	public void roll(Player p) {
		p.roll();
		rollCount++;
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

	public void reroll(Player p) {
		reroll(p, 1, 2, 3, 4, 5);
	}

	public void reroll(Player p, int... is) {
		if (rollCount >= 3) {
			throw new IllegalStateException("Cannot reroll more than twice");
		}
		p.reroll(is);
		rollCount++;
	}
	
	public String promptPlayer(Player p) {
		if (p != getCurrentPlayer()) {
			return "It's not your turn" + Yahtzee.EOL + "Please wait for " + getCurrentPlayer().getName() + "to finish!"
					+ Yahtzee.EOL;
		}
		if (awaitingIndexSet) {
			return "Please enter the dice position that you want to hold. Please separate each number with a <<SPACE>>";
		}
		if (rollCount == 0) {
			return "Press ENTER to Roll!" + Yahtzee.EOL;
		}
		if (rollCount >= 1 && rollCount <= 2) {
			return "(1) Reroll everything" + Yahtzee.EOL + "(2) Reroll by index" + Yahtzee.EOL + "(3) Score"
					+ Yahtzee.EOL;
		} else
			return "(3) Score" + Yahtzee.EOL;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Game: ");
		sb.append(this.getName());
		sb.append(Yahtzee.EOL);
		sb.append(getScoresheets());
		sb.append(Yahtzee.EOL);
		if (hasEnded()) {
			sb.append("Winner is: ");
			sb.append(getWinner());
			sb.append(Yahtzee.EOL);
		}
		return sb.toString();
	}



}
