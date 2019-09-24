package jf.comp5104.yahtzee;

import java.util.Map;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;

public class Scoresheet implements Comparable<Scoresheet> {

	private String name;
	private Map<Integer, Integer> sheet; // map from category to score

	private boolean bonus;
	private int yahtzees;
	private int round;

	static String[] Categories = { "Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "ThreeOfAKind", "FourOfAKind",
			"FullHouse", "Small Straight", "Large Straight", "Chance", "Yahtzee" };

	public Scoresheet() {
		this("");
	}

	public Scoresheet(String str) {
		sheet = new HashMap<Integer, Integer>(13);
		name = str;
		this.round = 1;
	}

	public String getName() {
		return name;
	}

	private void score(int i, int j) {
		sheet.put(i, j);
	}

	public void score(Roll r, int i) throws IndexOutOfBoundsException, AlreadyScoredThereException {
		if (i < 1 || i > 13) {
			throw new IndexOutOfBoundsException("Invalid category for scoring");
		}
		if (i != 13 && sheet.containsKey(i)) {
			throw new AlreadyScoredThereException();
		}
		if (i == 13 && sheet.containsKey(i)) {
			if (sheet.get(i) == 0 || !r.hasYahtzee()) {
				throw new AlreadyScoredThereException();
			}
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

	public void setRound(int i) {
		this.round = i;
	}

	public int getRound() {
		return round;
	}

	private void checkForBonus() {
		setBonus(calculateTotalUpperSection() >= 63);
	}

	private void setBonus(boolean b) {
		bonus = b;
	}

	private int calculateTotalUpperSection() {
		return sheet.entrySet().stream().filter(k -> k.getKey() >= 1 && k.getKey() <= 6).map(Map.Entry::getValue)
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
		return sheet.entrySet().stream().filter(k -> k.getKey() > 6).map(Map.Entry::getValue).reduce(Integer::sum)
				.orElse(0) + yahtzees * 100;
	}

	public int getTotal() {
		return getUpperTotal2() + getLowerTotal();
	}

	// get score from a particular category
	public int getScore(int i) {
		return sheet.getOrDefault(i, 0);
	}

	// need to distinguish between zero and not scored yet
	public String getScoreString(int i) {
		return sheet.get(i) != null ? sheet.get(i).toString() : " ";
	}

	@Override
	public int compareTo(Scoresheet o) {
		return getTotal() - o.getTotal();
	}

	private static final String lSep = "|";

	public String toString() {
		StringBuilder sb = new StringBuilder();

		// construct horizontal line
		for (int i = 0; i < 121; i++) {
			sb.append("-");
		}
		final String line = sb.toString();

		// clear builder
		sb.delete(0, sb.length());

		// formatter lets you specify width and stuff for pretty printing
		Formatter formatter = new Formatter(sb);

		Iterator<String> it = Arrays.asList(Categories).iterator();
		int i = 1;
		sb.append(line);
		sb.append(Yahtzee.EOL);
		sb.append(lSep);
		formatter.format(" %1$-19s : %2$-15s |", "Player Name", getName());
		formatter.format(" %1$-19s : %2$-15d |", "Current Score", getTotal());
		formatter.format(" %1$-19s : %2$-15d |", "Current Round", getRound());
		sb.append(Yahtzee.EOL);
		sb.append(line);
		sb.append(Yahtzee.EOL);
		sb.append(lSep);
		while (it.hasNext()) {
			formatter.format(" (%1$-2d) %2$-14s : %3$-15s |", i, it.next(), getScoreString(i));
			if (i % 3 == 0) {
				sb.append(Yahtzee.EOL);
				sb.append(line);
				sb.append(Yahtzee.EOL);
				sb.append(lSep);
			}

			if (i == 6) {
				// print Top Totals and Bonus
				formatter.format(" %1$-19s : %2$-15d |", "Top Sub-Total", getUpperTotal1());
				formatter.format(" %1$-19s : %2$-15s |", "Top Bonus", bonus ? "35" : " ");
				formatter.format(" %1$-19s : %2$-15d |", "Top Sub-Total", getUpperTotal2());
				sb.append(Yahtzee.EOL);
				sb.append(line);
				sb.append(Yahtzee.EOL);
				sb.append(lSep);
			}

			i++;
		}
		formatter.format(" %1$-19s : %2$-15d ", " Bonus Yahtzees", yahtzees);
		for (int j = 0; j < 11; j++) {
			formatter.format("|%1$3s", yahtzees > j ? "X" : " ");
		}
		formatter.close();
		sb.append(Yahtzee.EOL);
		sb.append(line);
		sb.append(Yahtzee.EOL);
		return sb.toString();
	}

	public void setName(String name) {
		this.name = name;
	}
}