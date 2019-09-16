package jf.comp5104.yahtzee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jf.comp5104.yahtzee.Die;

// A roll is a collection of 5 dice
public class Roll {

	private List<Die> diceList = new ArrayList<Die>(5);
	private HashMap<Integer, Integer> frequencyMap = new HashMap<>(5);

	public Roll() {
		diceList = new ArrayList<Die>(5);
		for (int i = 0; i < 5; i++) {
			diceList.add(new Die());
		}
		computeFrequency();
	}

	public Roll(int... is) throws IllegalStateException {
		if (is.length > 5)
			throw new IllegalStateException("A roll has 5 dice.");
		for (int i : is) {
			diceList.add(new Die(i));
		}
		computeFrequency();
	}

	public void reroll(int... is) throws IllegalStateException {
		if (is.length > 5)
			throw new IllegalStateException("Cannot reroll more than 5 dice.");
		for (int i : is) {
			if (i < 1 || i > 6)
				throw new IllegalStateException("Invalid index for reroll");
			diceList.get(i).roll();
		}
		computeFrequency();
	}

	// for testing only change to package visibility?
	void set(int i, int j) throws IllegalStateException, IndexOutOfBoundsException {
		if (i < 1 || i > 5)
			throw new IndexOutOfBoundsException("Attempt to set die " + i + " failed: out of bounds");
		if (j < 1 || j > 6)
			throw new IllegalStateException("Attempt to set die " + i + " to " + j + " failed: invalid value");
		diceList.get(i - 1).set(j);
		computeFrequency();
	}

	private void computeFrequency() {
		frequencyMap.clear();
		diceList.stream().mapToInt(Die::getValue)
				.forEach((n) -> frequencyMap.put(n, ((int) frequencyMap.getOrDefault(n, 0) + 1)));
	}

	public boolean contains(int n) {
		return frequencyMap.get(n) != null;
	}

	public int sum() {
		return diceList.stream().mapToInt(Die::getValue).reduce(0, Integer::sum);
	}

	public int size() {
		return diceList.size();
	}

	public boolean equals(Roll or) {
		List<Die> one = new ArrayList<Die>(or.diceList);
		List<Die> two = new ArrayList<Die>(diceList);
		Collections.sort(one);
		Collections.sort(two);
		return one.equals(two);
	}

	// roll contains i copies of j
	public boolean hasMultiple(int i, int j) {
		return (int) frequencyMap.getOrDefault(j, 0) == i;
	}

	public boolean hasFullHouse() {
		Set<Map.Entry<Integer, Integer>> entrySet = frequencyMap.entrySet();
		int frequencyOfFirstFace = ((Map.Entry<Integer, Integer>) entrySet.iterator().next()).getValue();
		// we know we have a full house
		// 1) we only have two kinds of cards/faces
		// 2) we have at least two of one kind and no more than 3.
		// 3) or we have 5 of a kind.
		return frequencyMap.size() == 1
				|| (frequencyMap.size() == 2 && 
				(frequencyOfFirstFace >= 2 && frequencyOfFirstFace <= 3));
	}

	public String toString() {
		StringBuilder sbuilder1 = new StringBuilder();
		StringBuilder sbuilder2 = new StringBuilder();
		StringBuilder sbuilder3 = new StringBuilder();
		for (Die d : diceList) {
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
		sbuilder2.append(Yahtzee.EOL);
		sbuilder3.append(Yahtzee.EOL);
		sbuilder1.append(sbuilder2.toString());
		sbuilder1.append(sbuilder3.toString());
		// Debug for toString() test.
		// System.out.println("toString()");
		// System.out.println(sbuilder1.toString());
		return sbuilder1.toString();
	}

	private int countStraight() {
		// first sort dice
		Iterator<Integer> sortedIterator = diceList.stream().mapToInt(Die::getValue).sorted().iterator();
		;
		// then count successors
		int countStraight = 0;
		int i = sortedIterator.next();
		int j;
		// needed to debug off by one error:
		// at end of while, countStraight == 4 means 4 successive cards -> full
		// straight
		// System.out.println("Counting Straight");
		// System.out.println(i);
		while (sortedIterator.hasNext()) {
			j = sortedIterator.next();
			// System.out.println(j);
			if (i + 1 == j) {
				countStraight++;
			}
			i = j;
		}
		// System.out.println("Count: " + countStraight);
		return countStraight;
	}

	public boolean hasSmallStraight() {
		return countStraight() >= 3;
	}

	public boolean hasLargeStraight() {
		return countStraight() == 4;
	}

	public boolean has3OfAKind() {
		return countMaxMultiple() >= 3;
	}

	public boolean has4OfAKind() {
		return countMaxMultiple() >= 4;
	}

	private int countMaxMultiple() {
		return frequencyMap.entrySet().stream().mapToInt((me) -> me.getValue()).max().orElse(0);
	}

	public boolean hasYahtzee() {
		return frequencyMap.size() == 1;
	}

	public void set(int... is) throws IllegalStateException {
		if (is.length != 5)
			throw new IllegalStateException("Ambiguous call to Roll.set(int[]) : need 5 ints");
		diceList.clear();
		for (int i : is) {
			diceList.add(new Die(i));
		}
		computeFrequency();
	}

	public int getMultiple(int i) {
		return frequencyMap.getOrDefault(i, 0);
	}
}
