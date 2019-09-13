package jf.comp5104.yahtzee;

import java.util.Random;

public class Dice {

	private int val;
	private static Random rng = new Random();
	private final String topAndBottom = "-----";
	private final String leftSide = "| ";
	private final String rightSide = " |";
	private static final String EOL = System.getProperty("line.separator");

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
		sbuilder.append(topAndBottom);
		sbuilder.append(EOL);
		sbuilder.append(leftSide);
		sbuilder.append(getValue());
		sbuilder.append(rightSide);
		sbuilder.append(EOL);
		sbuilder.append(topAndBottom);
		sbuilder.append(EOL);
		return sbuilder.toString();
	}
}
