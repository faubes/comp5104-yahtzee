package jf.comp5104.yahtzee;

import java.util.Random;

public class Dice {

	private int val;
	private static Random rng = new Random();
	static final String topAndBottom = "-----";
	static final String leftSide = "| ";
	static final String rightSide = " |";

	public Dice() {
		this.roll();
	}

	public Dice(int n) throws IllegalStateException {
		if (n < 1 || n > 6)
			throw new IllegalStateException("Assuming D6 are used. Cannot roll a " + n);
		val = n;
	}

	public int roll() {
		val = rng.nextInt(6) + 1;
		return val;
	}

	public int getValue() {
		return val;
	}

	public String toString() {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(Dice.topAndBottom);
		sbuilder.append(Yahtzee.EOL);
		sbuilder.append(Dice.leftSide);
		sbuilder.append(getValue());
		sbuilder.append(Dice.rightSide);
		sbuilder.append(Yahtzee.EOL);
		sbuilder.append(Dice.topAndBottom);
		sbuilder.append(Yahtzee.EOL);
		return sbuilder.toString();
	}
}
