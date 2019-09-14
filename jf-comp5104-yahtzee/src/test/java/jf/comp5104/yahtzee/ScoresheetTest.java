package jf.comp5104.yahtzee;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScoresheetTest {

	@Test
	public void testScoresheetConstructorWithoutParameters() {
		Scoresheet s = new Scoresheet();
		assertEquals("Default player name", s.getName(), "");
	}
	
	@Test
	public void testScoresheetConstructorWithName() {
		Scoresheet s = new Scoresheet("Joe");
		assertEquals("Scoresheet for Joe", s.getName(), "Joe");
	}
	
	@Test
	public void testNewScoresheetHasZeroScore() {
		Scoresheet s = new Scoresheet();
		assertEquals("Total of upper section: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper section (with bonus): 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
		assertEquals("Total:", s.getTotal(), 0);
	}
	
	@Test
	public void testScoreOnes() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		Scoresheet s = new Scoresheet();
		s.score(r, 1);
		//Rolled 5 ones, scored them as ones,
		assertEquals("Got 5 points in that category", s.getScore(1), 5);
		assertEquals("Total of upper sections: 5", s.getUpperTotal1(), 5);
		assertEquals("Total of upper sections: 5", s.getUpperTotal2(), 5);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
		assertEquals("Total:", s.getTotal(), 5);
	}

	@Test
	public void testScoreOnesInTwos() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		Scoresheet s = new Scoresheet();
		s.score(r, 1);
		//Rolled 5 ones, scored them as twos
		assertEquals("got 0 points in that category", s.getScore(2), 0);
		assertEquals("Total of upper sections: 5", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 5", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
		assertEquals("Total:", s.getTotal(), 0);
	}

	// restricted scoring to valid categories 1-13
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidScore() {
		Roll r = new Roll(1, 1,1,1,1);
		Scoresheet s = new Scoresheet();
		s.score(r, 0);
	}
	
	@Test
	public void testYahtzee() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		Scoresheet s = new Scoresheet();
		s.score(r, 13);
		//Rolled 5 ones, scored them as Yahtzee, 
		assertEquals("got 50 points in that category", s.getScore(13), 50);
		assertEquals("Total of upper sections: 5", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 5", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
		assertEquals("Total:", s.getTotal(), 0);
	}

}
