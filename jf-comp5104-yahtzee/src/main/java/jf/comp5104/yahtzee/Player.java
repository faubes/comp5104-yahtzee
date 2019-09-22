package jf.comp5104.yahtzee;


public class Player implements Comparable<Player> {
	static private int playerCount = 0;
	String username;
	Scoresheet scoresheet;
	Roll roll;
	private boolean connected;

	public Player(String username) {
		this.username = username;
		this.scoresheet = new Scoresheet(username);
	}
	
	public Player() {
		this("New Player " + (++playerCount));
	}

	public String getName() {
		return username;
	}
	
	public void setName(String name) {
		this.username = name;
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

	public Scoresheet getScoresheet() {
		return scoresheet;
	}

	@Override
	public int compareTo(Player o) {
		return this.scoresheet.compareTo(o.getScoresheet());
	}

	public void roll() {
		this.roll = new Roll();
		
	}

	public void score(int i) throws AlreadyScoredThereException, IllegalArgumentException {
		scoresheet.score(roll, i);
		
	}

	public void reroll(int ... is) {
		roll.reroll(is);
		
	}

	public Roll getRoll() {
		return roll;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(getName());
		sb.append(" (");
		sb.append(getScore());
		sb.append(")");
		sb.append(Yahtzee.EOL);
		return sb.toString();
	}
}
