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

	private void scoreZero(int i) {
		sheet.set(i, 0);
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
				if (r.has3OfAKind()) {
					score(i, r.sum());
				}
				else {
					scoreZero(i);
				}
				break;
			case 8:
				if (r.has4OfAKind()) {
					score(i, r.sum());
				}
				else {
					scoreZero(i);
				}
				break;
			case 9:
				if (r.hasFullHouse()) {
					score(i, 25));
				}
				else {
					scoreZero(i);
				}
				break;
			case 10:
				if (r.hasSmallStraight()) {
					score(i, 30);
				}
				else {
					scoreZero(i);
				}
				break;
			case 11:
				if (r.hasLargeStraight()) {
					score(i, 40);
					
				}
				else {
					scoreZero(i);
				}
				break;
			case 12:
				if (r.hasYahtzee()) {
					score(i, 50);
				}
				else {
					scoreZero(i);
				}
				break;
				//chance
			case 13:
				score(i, r.sum());
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