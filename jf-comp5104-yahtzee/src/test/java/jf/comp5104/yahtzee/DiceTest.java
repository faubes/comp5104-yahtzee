package jf.comp5104.yahtzee;

import jf.comp5104.yahtzee.Die;

import org.junit.Test;

import static org.junit.Assert.*;


public class DiceTest {

	@Test
	public void testDiceConstructorWithoutParameters() {
		Die d = new Die();
		assertTrue("New dice automatically rolled", d.getValue() >= 1 && d.getValue() <= 6);
		// next step: check that a roll returns an integer in [1,6]
	}
	
	// want to be able to construct specific dice for testing
	@Test
	public void testDiceConstructorWithSpecifiedValue() {
		Die d = new Die(5);
		assertEquals("Dice has value 5", d.getValue(), 5);
	}

	// shouldn't create dice in bad state
	@Test(expected = IllegalStateException.class)
	public void testDiceConstructorWithBadValue() {
		Die d = new Die(10);
	}
	
	
	@Test
	public void testToString() {
		Die d = new Die(1);
		String expected = "-----\n" +
						  "| 1 |\n" +
						  "-----\n";
		assertEquals("Dice has string representation", d.toString(), expected);
	}

	@Test
	public void testRoll() {
		Die d = new Die();
		d.roll();
		assertTrue("Dice has non-zero value", d.getValue() >= 1 && d.getValue() <= 6);
	}
	
	
}
