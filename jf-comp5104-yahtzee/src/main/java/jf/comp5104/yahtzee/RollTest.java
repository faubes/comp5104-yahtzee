package jf.comp5104.yahtzee;

import static org.junit.Assert.*;

import org.junit.Test;

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
		assertEquals("String representation", r.toString(),
				"-----\t-----\t-----\t-----\t-----\t\n"+
				"| 1 |\t| 1 |\t| 1 |\t| 1 |\t| 1 |\t\n"+
				"-----\t-----\t-----\t-----\t-----\t\n");
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
	
	@Test(expected = IllegalStateException.class) 
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
	
	
}