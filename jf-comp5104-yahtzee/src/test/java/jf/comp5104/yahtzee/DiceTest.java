package jf.comp5104.yahtzee;

import jf.comp5104.yahtzee.Dice;

import org.junit.Test;

import static org.junit.Assert.*;


public class DiceTest {

	@Test
	public void testDiceConstructorWithoutParameters() {
		Dice d = new Dice();
		assertTrue("New dice automatically rolled", d.getValue() >= 1 && d.getValue() <= 6);
		// next step: check that a roll returns an integer in [1,6]
	}
	
	// want to be able to construct specific dice for testing
	@Test
	public void testDiceConstructorWithSpecifiedValue() {
		Dice d = new Dice(5);
		assertEquals("Dice has value 5", d.getValue(), 5);
	}

	// shouldn't create dice in bad state
	@Test(expected = IllegalStateException.class)
	public void testDiceConstructorWithBadValue() {
		Dice d = new Dice(10);
	}
	
	
	@Test
	public void testToString() {
		Dice d = new Dice(1);
		String expected = "-----\n" +
						  "| 1 |\n" +
						  "-----\n";
		assertEquals("Dice has string representation", d.toString(), expected);
	}

	@Test
	public void testRoll() {
		Dice d = new Dice();
		d.roll();
		assertTrue("Dice has non-zero value", d.getValue() >= 1 && d.getValue() <= 6);
	}
	
	
}
