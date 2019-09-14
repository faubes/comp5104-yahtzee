package jf.comp5104.yahtzee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Scoresheet {

	private String name;
	private Map<Integer, Integer> sheet; // map from category to score

	private boolean bonus;
	private int yahtzees;

	public Scoresheet() {
		this("");
	}

	public Scoresheet(String str) {
		sheet = new HashMap<Integer, Integer>(13);
		name = str;
	}

	public String getName() {
		return name;
	}

	private void score(int i, int j) {
		sheet.put(i, j);
	}

	private void scoreZero(Integer i) {
		sheet.put(i, 0);
	}

	public void score(Roll r, int i) {
		if (i < 1 || i > 13)
			throw new IndexOutOfBoundsException("Invalid category for scoring");
		if (i != 13 && sheet.containsKey(i))
			throw new IllegalArgumentException("Already scored in that category");
		if (i == 13 && sheet.containsKey(i)) {
			// another Yathzee
			yahtzees++;
			return;
		}

		int score = 0;

		switch (i) {
		case 7:
			if (r.has3OfAKind()) {
				score = r.sum();
			}
			break;
		case 8:
			if (r.has4OfAKind()) {
				score = r.sum();
			}
			break;
		case 9:
			if (r.hasFullHouse()) {
				score = 25;
			}
			break;
		case 10:
			if (r.hasSmallStraight()) {
				score = 30;
			}
			break;
		case 11:
			if (r.hasLargeStraight()) {
				score = 40;
			}
			break;
		// chance is twelve: add dice
		case 12:
			score = r.sum();
			break;
		// yahtzee! 5 of a kind
		case 13:
			if (r.hasYahtzee()) {
				score = 50;
			}
			break;
		default:
			score = r.getMultiple(i) * i;
		}
		score(i, score);
		checkForBonus();
	}

	private void checkForBonus() {
		setBonus(calculateTotalUpperSection() >= 63);
	}

	private void setBonus(boolean b) {
		bonus = b;
	}

	private int calculateTotalUpperSection() {
		return sheet.entrySet().stream().filter((k) -> k.getKey() >= 1 && k.getKey() <= 6).map((m) -> m.getValue())
				.reduce(Integer::sum).orElse(0);
	}

	public int getUpperTotal1() {
		return calculateTotalUpperSection();
	}

	// with bonus, if applicable
	public int getUpperTotal2() {
		return calculateTotalUpperSection() + (bonus ? 35 : 0);
	}

	public int getLowerTotal() {
		return sheet.entrySet().stream().filter(p -> p.getKey() > 6).map(p -> p.getValue()).reduce(Integer::sum)
				.orElse(0) + yahtzees * 100;
	}

	public int getTotal() {
		return getUpperTotal2() + getLowerTotal();
	}

	// get score from a particular category
	public int getScore(int i) {
		return sheet.getOrDefault(i, 0);
	}

}