package jf.comp5104.yahtzee;

import java.util.ArrayList;
import java.util.List;

import jf.comp5104.yahtzee.Dice;

// A roll is a collection of 5 dice
public class Roll {

	private List<Dice> l = new ArrayList<Dice>();
	
	public Roll() {
		l = new ArrayList<Dice>(5);
		for (int i = 0; i < 5; i++) {
			l.add(new Dice());
		}
	}
	
	public Roll(int[] is) throws IllegalStateException {
		if (is.length > 5) throw new IllegalStateException("A roll has 5 dice.");
		for (int i : is) {
			l.add(new Dice(i));
		}
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
		for (Dice d : l) {
			sbuilder1.append(Dice.topAndBottom);
			sbuilder1.append(Yahtzee.TAB);
			sbuilder2.append(Dice.leftSide);
			sbuilder2.append(d.getValue());
			sbuilder2.append(Dice.rightSide);
			sbuilder2.append(Yahtzee.TAB);
			sbuilder3.append(Dice.topAndBottom);
			sbuilder3.append(Yahtzee.TAB);
		}
		sbuilder1.append(Yahtzee.EOL);
		sbuilder1.append(sbuilder2.toString());
		sbuilder1.append(Yahtzee.EOL);
		sbuilder1.append(sbuilder3.toString());
		return sbuilder1.toString();
	}
	
}
