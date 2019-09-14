package jf.comp5104.yahtzee;

import jf.comp5104.yahtzee.Die;

import org.junit.Test;

import static org.junit.Assert.*;


public class DieTest {

	@Test
	public void testDieConstructorWithoutParameters() {
		Die d = new Die();
		assertTrue("New Die automatically rolled", d.getValue() >= 1 && d.getValue() <= 6);
		// next step: check that a roll returns an integer in [1,6]
	}
	
	// want to be able to construct specific Die for testing
	@Test
	public void testDieConstructorWithSpecifiedValue() {
		Die d = new Die(5);
		assertEquals("Die has value 5", d.getValue(), 5);
	}

	// shouldn't create Die in bad state
	@Test(expected = IllegalStateException.class)
	public void testDieConstructorWithBadValue() {
		@SuppressWarnings("unused")
		Die d = new Die(10);
	}
	
	
	@Test
	public void testToString() {
		Die d = new Die(1);
		String expected = "-----" + Yahtzee.EOL +
						  "| 1 |" + Yahtzee.EOL +
						  "-----" + Yahtzee.EOL;
		assertEquals("Die has string representation", d.toString(), expected);
	}

	@Test
	public void testRoll() {
		Die d = new Die();
		d.roll();
		assertTrue("Die has non-zero value", d.getValue() >= 1 && d.getValue() <= 6);
	}
	
	
}
