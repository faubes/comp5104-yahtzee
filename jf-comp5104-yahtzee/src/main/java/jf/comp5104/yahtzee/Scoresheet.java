package jf.comp5104.yahtzee;

import java.util.ArrayList;

public class Scoresheet {

	private String name;
	private ArrayList<Integer> sheet; // store scores in simple array
	private boolean bonus;
	private int yahtzees;

	public Scoresheet() {
		this("");
	}

	public Scoresheet(String str) {
		sheet = new ArrayList<>(13);
		name = str;
	}

	public String getName() {
		return name;
	}

	private void score(int i, int j) {
		sheet.set(i, j);
	}

	private int calculateTotalUpperSection() {
		return sheet.stream().limit(6).reduce(Integer::sum).orElse(0);
	}

	private void checkForBonus() {
		if (calculateTotalUpperSection() > 35) {
			bonus = true;
		} else {
			bonus = false;
		}
	}

	public void score(Roll r, int i) {
		if (i >= 1 && i <= 6) {
			score(i, r.getMultiple(i) * i);
			checkForBonus();
		} else {
			switch (i) {
			case 7:
				if (r.hasTriple()) {
					score(i, r.sum());
				}
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
			case 12:
				break;
			case 13:
				break;
			default:
				throw new IllegalArgumentException("Invalid category for scoring");

			}
		}
	}

	public int getUpperTotal1() {
		return calculateTotalUpperSection();
	}
	
	// with bonus, if applicable
	public int getUpperTotal2() {
		return calculateTotalUpperSection() + (bonus ? 35 : 0);
	}

	public int getLowerTotal() {
		return sheet.stream().skip(6).reduce(Integer:sum);
	}
}