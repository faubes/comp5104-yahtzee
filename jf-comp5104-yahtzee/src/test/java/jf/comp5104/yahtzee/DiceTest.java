package jf.comp5104.yahtzee;

import jf.comp5104.yahtzee.Dice;

import org.junit.Test;

import static org.junit.Assert.*;


public class DiceTest {

	@Test
	public void test() {
		Dice d = new Dice();
<<<<<<< HEAD
		AssertEquals(0, d.roll(), "dice without rng always rolls 0");
=======
		assertEquals("dice without rng always rolls 0", 0, d.roll());
>>>>>>> tmp
		// next step: check that a roll returns an integer in [1,6]
	}


}
