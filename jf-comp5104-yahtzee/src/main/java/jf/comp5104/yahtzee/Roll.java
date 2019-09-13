package jf.comp5104.yahtzee;

import java.util.ArrayList;
import java.util.List;

import jf.comp5104.yahtzee.Die;

// A roll is a collection of 5 dice
public class Roll {

	private List<Die> l = new ArrayList<Die>();
	
	public Roll() {
		l = new ArrayList<Die>(5);
		for (int i = 0; i < 5; i++) {
			l.add(new Die());
		}
	}
	
	public Roll(int ... is) throws IllegalStateException {
		if (is.length > 5) throw new IllegalStateException("A roll has 5 dice.");
		for (int i : is) {
			l.add(new Die(i));
		}
	}

	public void reroll(int ... is) throws IllegalStateException {
		if (is.length > 5) throw new IllegalStateException("Cannot reroll more than 5 dice.");
		for (int i : is) {
			if (i < 1 || i > 6) throw new IllegalStateException("Invalid index for reroll");
			l.get(i).roll();
		}
	}
	
	public int sum() {
		return l.stream().mapToInt(Die::getValue).reduce(0, Integer::sum);
	}
	
	public int size() {
		return l.size();
	}
	
	public boolean contains(int n) {
		return l.contains(n);
	}
	
	public String toString() {
		StringBuilder sbuilder1 = new StringBuilder();
		StringBuilder sbuilder2 = new StringBuilder();
		StringBuilder sbuilder3 = new StringBuilder();
		for (Die d : l) {
			sbuilder1.append(Die.topAndBottom);
			sbuilder1.append(Yahtzee.TAB);
			sbuilder2.append(Die.leftSide);
			sbuilder2.append(d.getValue());
			sbuilder2.append(Die.rightSide);
			sbuilder2.append(Yahtzee.TAB);
			sbuilder3.append(Die.topAndBottom);
			sbuilder3.append(Yahtzee.TAB);
		}
		sbuilder1.append(Yahtzee.EOL);
		sbuilder1.append(sbuilder2.toString());
		sbuilder1.append(Yahtzee.EOL);
		sbuilder1.append(sbuilder3.toString());
		return sbuilder1.toString();
	}
	
}