package jf.comp5104.yahtzee;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiceTest {

	@Test
	public void test() {
		Dice d = new Dice();
		AssertEquals(0, d.roll(), "dice without rng always rolls 0");
		// next step: check that a roll returns an integer in [1,6]
	}


}
