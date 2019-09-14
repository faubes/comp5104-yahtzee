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
		} else if (i >= 1 && i <= 6) {
			score(i, r.getMultiple(i) * i);
			checkForBonus();
		} else {
			switch (i) {
			case 7:
				if (r.has3OfAKind()) {
					score(i, r.sum());
				} else {
					scoreZero(i);
				}
				break;
			case 8:
				if (r.has4OfAKind()) {
					score(i, r.sum());
				} else {
					scoreZero(i);
				}
				break;
			case 9:
				if (r.hasFullHouse()) {
					score(i, 25);
				} else {
					scoreZero(i);
				}
				break;
			case 10:
				if (r.hasSmallStraight()) {
					score(i, 30);
				} else {
					scoreZero(i);
				}
				break;
			case 11:
				if (r.hasLargeStraight()) {
					score(i, 40);

				} else {
					scoreZero(i);
				}
				break;
			// chance is twelve: add dice
			case 12:
				score(i, r.sum());
				break;
			// yahtzee! 5 of a kind
			case 13:
				if (r.hasYahtzee()) {
					score(i, 50);
				} else {
					scoreZero(i);
				}
				break;
			// chance
			default:
				// shouldn't ever get here
				throw new IllegalArgumentException("Invalid category for scoring");

			}
		}
	}

	private void checkForBonus() {
		if (calculateTotalUpperSection() >= 63) {
			bonus = true;
		} else {
			bonus = false;
		}
	}

	private int calculateTotalUpperSection() {
		return sheet.entrySet().stream().filter((k) -> k.getValue() >= 1 && k.getValue() <= 6).map((m) -> m.getValue())
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
				.orElse(0);
	}

	public int getTotal() {
		return getUpperTotal2() + getLowerTotal() + yahtzees * 100;
	}

	// get score from a particular category
	public int getScore(int i) {
		return sheet.getOrDefault(i, 0);
	}

}