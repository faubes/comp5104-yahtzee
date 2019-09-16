package jf.comp5104.yahtzee;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;

import org.junit.*;

public class ScoresheetTest {

	/* Construct a few objects to manipulate */
	private Scoresheet s;
	private Scoresheet s2;
	
	@Before
	public void setUp() {
		Scoresheet s = new Scoresheet();
		Scoresheet s2 = new Scoresheet();
	}
	
	@After
	public void tearDown() {	} // explicitly destroy objects?
	
	@Test
	public void testScoresheetConstructorWithoutParameters() {
		assertEquals("Default player name", s.getName(), "");
	}
	
	@Test
	public void testScoresheetConstructorWithName() {
		s = new Scoresheet("Joe");
		assertEquals("Scoresheet for Joe", s.getName(), "Joe");
	}
	
	@Test
	public void testNewScoresheetHasZeroScore() {
		assertEquals("Total of upper section: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper section (with bonus): 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
		assertEquals("Total:", s.getTotal(), 0);
	}
	
	@Test
	public void testScoreOnes() {
		Roll r = new Roll(1, 1, 1, 1, 1);
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
		s.score(r, 2);
		//Rolled 5 ones, scored them as twos
		assertEquals("got 0 points in that category", s.getScore(2), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
		assertEquals("Total:", s.getTotal(), 0);
	}

	// restricted scoring to valid categories 1-13
	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidScore() {
		Roll r = new Roll(1, 1,1,1,1);
		s.score(r, 0);
	}
	
	@Test
	public void testYahtzee() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		s.score(r, 13);
		//Rolled 5 ones, scored them as Yahtzee, 
		assertEquals("got 50 points in that category", s.getScore(13), 50);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 50);
		assertEquals("Total:", s.getTotal(), 50);
	}

	@Test
	public void testMultipleYahtzees() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		s.score(r, 13);
		//Rolled 5 ones, scored them as Yahtzee, 
		assertEquals("got 50 points in that category", s.getScore(13), 50);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 50);
		assertEquals("Total:", s.getTotal(), 50);
		s.score(r, 13);
		//Rolled 5 ones, scored them as Yahtzee, 
		assertEquals("got 50 points in that category", s.getScore(13), 50);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 150", s.getLowerTotal(), 150);
		assertEquals("Total:", s.getTotal(), 150);
		s.score(r, 13);
		//Rolled 5 ones, scored them as Yahtzee, 
		assertEquals("got 50 points in that category", s.getScore(13), 50);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 250);
		assertEquals("Total:", s.getTotal(), 250);
		
	}
	@Test
	public void test3OfAKind() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		s.score(r, 7);
		//Rolled 5 ones, scored them as 3OfAKind, 
		assertEquals("got 5 points in that category", s.getScore(7), 5);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 5);
		assertEquals("Total:", s.getTotal(), 5);
		s.score(r, 8);
		//Rolled 5 ones, scored them as 4OfAKind, 
		assertEquals("got 5 points in that category", s.getScore(8), 5);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 10);
		assertEquals("Total:", s.getTotal(), 10);
		s.score(r, 9);
		//Rolled 5 ones, scored them as FullHouse, 
		assertEquals("got 25 points in that category", s.getScore(9), 25);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 35);
		assertEquals("Total:", s.getTotal(), 35);
		
	}
	
	public void testScoresheetsAreComparable() {
		Roll yahtzee6 = new Roll(6,6,6,6,6);
		Roll nothing = new Roll(1,4,2,5,6);
		s.score(yahtzee6, 13);
		s2.score(nothing, 1);
		assertTrue("Yahtzee is more points than nothing", s.compareTo(s2) > 0);
	}

}
