package jf.comp5104.yahtzee;

import static org.junit.Assert.*;

import org.junit.*;

import jf.comp5104.yahtzee.exceptions.AlreadyScoredThereException;

public class ScoresheetTest {

	/* Construct a few objects to manipulate */
	private Scoresheet s;
	private Scoresheet s2;

	@Before
	public void setUp() {
		s = new Scoresheet();
		s2 = new Scoresheet();
	}

	@After
	public void tearDown() {
	} // explicitly destroy objects?

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
		try {
			s.score(r, 1);
			// Rolled 5 ones, scored them as ones,
			assertEquals("Got 5 points in that category", s.getScore(1), 5);
			assertEquals("Total of upper sections: 5", s.getUpperTotal1(), 5);
			assertEquals("Total of upper sections: 5", s.getUpperTotal2(), 5);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
			assertEquals("Total:", s.getTotal(), 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testScoreOnesInTwos() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		try {
			s.score(r, 2);
			// Rolled 5 ones, scored them as twos
			assertEquals("got 0 points in that category", s.getScore(2), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
			assertEquals("Total:", s.getTotal(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// restricted scoring to valid categories 1-13
	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidScore() throws IndexOutOfBoundsException, AlreadyScoredThereException {
		Roll r = new Roll(1, 1, 1, 1, 1);
		s.score(r, 0);
	}

	@Test(expected = AlreadyScoredThereException.class)
	public void testNotAYathzee() throws Exception {
		Roll r = new Roll(2, 1, 1, 1, 1);
		s.score(r, 13);
		assertEquals("No points for non-yahtzee.", 0, s.getScore(13));
		r.set(1, 1, 1, 1, 1);
		s.score(r, 13); // not allowed to score there throw exception

	}

	@Test
	public void testYahtzee() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		try {
			s.score(r, 13);
			// Rolled 5 ones, scored them as Yahtzee,
			assertEquals("got 50 points in that category", s.getScore(13), 50);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 50);
			assertEquals("Total:", s.getTotal(), 50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test(expected = AlreadyScoredThereException.class)
	public void testNotYahtzee() throws IndexOutOfBoundsException, AlreadyScoredThereException {
		Roll r = new Roll(1, 1, 1, 1, 6);
			s.score(r, 13);
			// Rolled 4 ones, scored them as Yahtzee,
			assertEquals("got 0 points in that category", s.getScore(13), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
			assertEquals("Total:", s.getTotal(), 0);
			r.set(6,6,6,6,6);
			// cannot now score Yahtzee :(
			s.score(r, 13);
	}

	@Test
	public void testMultipleYahtzees() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		try {
			s.score(r, 13);
			// Rolled 5 ones, scored them as Yahtzee,
			assertEquals("got 50 points in that category", s.getScore(13), 50);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 50);
			assertEquals("Total:", s.getTotal(), 50);
			s.score(r, 13);
			// Rolled 5 ones, scored them as Yahtzee,
			assertEquals("got 50 points in that category", s.getScore(13), 50);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 150", s.getLowerTotal(), 150);
			assertEquals("Total:", s.getTotal(), 150);
			s.score(r, 13);
			// Rolled 5 ones, scored them as Yahtzee,
			assertEquals("got 50 points in that category", s.getScore(13), 50);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 250);
			assertEquals("Total:", s.getTotal(), 250);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(expected = AlreadyScoredThereException.class)
	public void testYahtzeeThenNot() throws IndexOutOfBoundsException, AlreadyScoredThereException {
		Roll r = new Roll(1, 1, 1, 1, 1);
		s.score(r, 13);
		// Rolled 5 ones, scored them as Yahtzee,
		assertEquals("got 50 points in that category", s.getScore(13), 50);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 50);
		assertEquals("Total:", s.getTotal(), 50);
		r.set(6, 6, 6, 6, 6);
		s.score(r, 13);
		// Rolled 5 6, scored them as Yahtzee,
		assertEquals("got 50 points in that category", s.getScore(13), 50);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 150", s.getLowerTotal(), 150);
		assertEquals("Total:", s.getTotal(), 150);
		r.set(1, 2, 3, 4, 5);
		s.score(r, 13); // throw exception
	}

	@Test
	public void testYahtzeesCanGoAnywhereInLower() {
		Roll r = new Roll(1, 1, 1, 1, 1);
		try {
			s.score(r, 7);
			// Rolled 5 ones, scored them as 3OfAKind,
			assertEquals("got 5 points in that category", s.getScore(7), 5);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 5);
			assertEquals("Total:", s.getTotal(), 5);
			s.score(r, 8);
			// Rolled 5 ones, scored them as 4OfAKind,
			assertEquals("got 5 points in that category", s.getScore(8), 5);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 10);
			assertEquals("Total:", s.getTotal(), 10);
			s.score(r, 9);
			// Rolled 5 ones, scored them as FullHouse,
			assertEquals("got 25 points in that category", s.getScore(9), 25);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 35);
			assertEquals("Total:", s.getTotal(), 35);
			// System.out.println(s.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNot3OfAKind() {
		Roll r = new Roll(1, 1, 2, 2, 3);
		try {
			s.score(r, 7);
			// Rolled 3 ones, scored them as 3OfAKind,
			assertEquals("got 0 points in that category", s.getScore(7), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
			assertEquals("Total:", s.getTotal(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3OfAKind() {
		Roll r = new Roll(1, 1, 1, 2, 2);
		try {
			s.score(r, 7);
			// Rolled 3 ones, scored them as 3OfAKind,
			assertEquals("got 7 points in that category", s.getScore(7), 7);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 7);
			assertEquals("Total:", s.getTotal(), 7);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNot4OfAKind() {
		try {
			Roll r = new Roll(1, 1, 1, 2, 2);
			s.score(r, 8);
			// Rolled 3 ones, scored them as 4OfAKind,
			assertEquals("got 0 points in that category", s.getScore(8), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
			assertEquals("Total:", s.getTotal(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test4OfAKind() {
		try {
			Roll r = new Roll(1, 1, 1, 1, 2);
			s.score(r, 8);
			// Rolled 3 ones, scored them as 4OfAKind,
			assertEquals("got 6 points in that category", s.getScore(8), 6);
			assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
			assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
			assertEquals("Total of lower section: 0", s.getLowerTotal(), 6);
			assertEquals("Total:", s.getTotal(), 6);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSmallStraight() throws IndexOutOfBoundsException, AlreadyScoredThereException {
		Roll r = new Roll(1,2,3,4,4);
		s.score(r, 10);
		assertEquals("got 30 points in that category", s.getScore(10), 30);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 30", s.getLowerTotal(), 30);
		assertEquals("Total:", s.getTotal(), 30);
		s = new Scoresheet();
		r.set(1,2,1,2,1); // not a smallStraight
		s.score(r, 10);
		assertEquals("got 0 points in that category", s.getScore(8), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 0", s.getLowerTotal(), 0);
		assertEquals("Total:", s.getTotal(), 0);	
	}
	
	@Test
	public void testLargeStraight() throws IndexOutOfBoundsException, AlreadyScoredThereException {
		Roll r = new Roll(1,2,3,4,5);
		s.score(r, 11);
		assertEquals("got 40 points in that category", s.getScore(11), 40);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 20", s.getLowerTotal(), 40);
		assertEquals("Total:", s.getTotal(), 40);
		s = new Scoresheet();
		r.set(6,3,4,5,2); // also a Large Straight but not in order
		s.score(r, 11);
		assertEquals("got 40 points in that category", s.getScore(11), 40);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 20", s.getLowerTotal(), 40);
		assertEquals("Total:", s.getTotal(), 40);
		r.set(6,3,4,5,1); // not a Large Straight
		s = new Scoresheet();
		s.score(r, 11);
		assertEquals("got 0 points in that category", s.getScore(11), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 20", s.getLowerTotal(), 0);
		assertEquals("Total:", s.getTotal(), 0);
		
	}

	
	@Test
	public void testScoreChance() throws IndexOutOfBoundsException, AlreadyScoredThereException {
		Roll r = new Roll(1,2,3,4,5);
		s.score(r, 12);
		assertEquals("got 15 points in that category", s.getScore(12), 15);
		assertEquals("Total of upper sections: 0", s.getUpperTotal1(), 0);
		assertEquals("Total of upper sections: 0", s.getUpperTotal2(), 0);
		assertEquals("Total of lower section: 20", s.getLowerTotal(), 15);
		assertEquals("Total:", s.getTotal(), 15);
		s = new Scoresheet();
		r.set(6,6,6,6,6); // also a Large Straight but not in order
		s.score(r, 12);
		assertEquals("got 30 points in that category", s.getScore(12), 30);
	}
	
	@Test
	public void testScoresheetsAreComparable() {
		Roll yahtzee6 = new Roll(6, 6, 6, 6, 6);
		Roll nothing = new Roll(1, 4, 2, 5, 6);
		try {
			s.score(yahtzee6, 13);
			s2.score(nothing, 1);
			assertTrue("Yahtzee is more points than nothing", s.compareTo(s2) > 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testToString() {
		Roll yahtzee6 = new Roll(6, 6, 6, 6, 6);
		try {
			s.score(yahtzee6, 13);
			s.score(yahtzee6, 12);
			// System.out.println(s.toString());
			assertTrue("String representation", s.toString() instanceof String);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
