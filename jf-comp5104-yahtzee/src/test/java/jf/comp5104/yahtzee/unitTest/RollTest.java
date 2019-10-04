package jf.comp5104.yahtzee.unitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import jf.comp5104.yahtzee.Roll;
import jf.comp5104.yahtzee.Yahtzee;

public class RollTest {

	@Test
	public void testRollConstructorWithoutParameters() {
		@SuppressWarnings("unused")
		Roll r = new Roll();
	}

	@Test
	public void testRollConstructorWithSpecifiedValues() {
		@SuppressWarnings("unused")
		Roll r = new Roll(new int[]{1,1,1,1,1});
	}
	
	@Test
	public void testRollHasSize() {
		Roll r = new Roll();
		assertEquals("A roll contains five dice", r.size(), 5);
	}
	
	@Test
	public void testRollHasToString() {
		Roll r = new Roll(new int[]{1,1,1,1,1});
		String testline1 = "-----\t-----\t-----\t-----\t-----\t";
		String testline2 = "| 1 |\t| 1 |\t| 1 |\t| 1 |\t| 1 |\t";
		String testline3 = "-----\t-----\t-----\t-----\t-----\t";
		// needed to test: apparently \n is not equal to System.getProperty("line.separator");
		//System.out.println("TEST:");
		//System.out.println(testline1);
		//System.out.println(testline2);
		//System.out.println(testline3);
		StringBuilder sb = new StringBuilder(testline1);
		sb.append(Yahtzee.EOL);
		sb.append(testline2);
		sb.append(Yahtzee.EOL);
		sb.append(testline3);
		sb.append(Yahtzee.EOL);
		assertEquals("String representation", r.toString(), sb.toString());
	}
	
	@Test
	public void testRollHasSum() {
		Roll r = new Roll(new int[]{1,2,3,4,5});
		assertEquals("Sums to 15", r.sum(), 15);
	}
	
	@Test
	public void testRollCanBeRerolledByIndex() {
		Roll r = new Roll();
		r.reroll(1);
	}
	
	@Test
	public void testRollCanBeRerolledByIndexSet() {
		Roll r = new Roll();
		r.reroll(new int[]{1, 2});
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCannotRerollMoreThanAll() {
		Roll r = new Roll();
		r.reroll(new int[]{1,2,3,4,5,6});
	}
	
	@Test(expected = IndexOutOfBoundsException.class) 
	public void testCannotRerollWithBadIndex() {
		Roll r = new Roll();
		r.reroll(8); // there are only 6 dice
	}
	
	@Test
	public void testRollContains() {
		Roll r = new Roll(new int[]{1,2,3,4,5});
		assertTrue("Does have a 1 ", r.contains(1));
		assertFalse("Does not contain a 6", r.contains(6));
	}
	
	@Test
	public void testHasPair() {
		Roll r = new Roll(new int[]{1,1,2,3,4});
		assertTrue("Contains a pair", r.hasMultiple(2, 1));
		assertFalse("Does not have a pair of two's", r.hasMultiple(2, 2));
	}
	
	@Test
	public void testHasFullHouse() {
		Roll r = new Roll(1, 1, 2, 2, 2);
		assertTrue("Has a full house", r.hasFullHouse());
		r.set(1, 1, 2, 2, 3); // set dice 5 to 3
		assertFalse("Does not have full house", r.hasFullHouse());
	}
	
	@Test
	public void testHasSmallStraight() {
		Roll r = new Roll(1, 2, 6, 3, 4);
		assertTrue("Has a small straight", r.hasSmallStraight());
		r.set(1, 2, 6, 3, 6); // set dice 5 to 3
		assertFalse("Does not have small straight", r.hasSmallStraight());
	}
	
	@Test
	public void testHasFullStraight1() {
		Roll r = new Roll(3, 2, 5, 1, 4);
		assertTrue("Has a full straight", r.hasLargeStraight());
		r.set(3, 2, 6, 1, 4); // set dice 5 to 3
		assertFalse("Does not have full straight", r.hasLargeStraight());
	}
	
	@Test
	public void testHasYahtzee() {
		Roll r = new Roll(1,1,1,1,1);
		assertTrue("Has Yahtzee", r.hasYahtzee());
		r.set(1,1,1,1,6);
		assertFalse("Has Yahtzee", r.hasYahtzee());
		r.set(6,6,6,6,6);
		assertTrue("Has Yahtzee", r.hasYahtzee());
		r.set(1,1,1,6,6);
		assertFalse("Has Yahtzee", r.hasYahtzee());
		
	}
}
