package jf.comp5104.yahtzee.unitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import jf.comp5104.yahtzee.Game;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.Yahtzee;

import org.junit.*;

public class GameTest {

	private Game g;
	private Player p1;
	private Player p2;
	private Player p3;

	@Before
	public void setUp() {
		g = new Game("Game 1");
		p1 = new Player("Joe");
		p2 = new Player("Jim");
		p3 = new Player("Jerry");
		g.addPlayer(p1);
		g.addPlayer(p2);
		g.addPlayer(p3);
	}

	@Test
	public void testGetRound() {
		assertTrue("New game in round 0 until first roll", g.getRound() == 0);
	}

	@Test
	public void testGetNumPlayers() {
		assertEquals("New game has three players", g.getNumberOfPlayers(), 3);
	}

	@Test
	public void testGetWinner() {
		assertTrue("GetWinner returns a player", g.getWinner() instanceof Player);
		// TODO: actually test if the returned player has best score
	}

	@Test
	public void testGetCurrentPlayer() {
		assertEquals("Get Current Player returns Player 1", g.getCurrentPlayer(), p1);
	}

	@Test
	public void testGetScoresheets() {
		assertTrue("Can get players' scoresheets", g.getScoresheets() instanceof String);
	}

	@Test
	public void testCanPlayARound() {
		try {
			g.start();
			assertFalse("Game has not yet ended", g.hasEnded());
			assertEquals("Round 1", g.getRound(), 1);
			assertEquals("Get Current Player returns Player 1", g.getCurrentPlayer(), p1);
			assertTrue("Game has started", g.hasStarted() && !g.hasEnded());
			g.roll(p1);
			g.score(p1, 1);
			assertEquals("now returns Player 2", g.getCurrentPlayer(), p2);
			g.roll(p2);
			// g.reroll(p2, new int[]{1,2,3,4,5});
			g.score(p2, 1);
			assertEquals("now returns Player 3", g.getCurrentPlayer(), p3);
			g.roll(p3);
			g.score(p3, 1);
			assertEquals("Round 2", g.getRound(), 2);
			assertEquals("Back to Player 1", g.getCurrentPlayer(), p1);
			g.quit();
			assertTrue("Game has ended", g.hasEnded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testPlayerCanReroll() {
		g.start();
		g.roll(p1);
		g.reroll(p1);
		g.reroll(p1, 2, 3);
		g.reroll(p1, 5); // can't reroll three times
	}

	@Test
	public void testPlay13Rounds() {
		g.start();
		for (int i = 1; i <= 13; i++) {
			try {
				assertEquals("Player 1's turn", g.getCurrentPlayer(), p1);
				g.roll(p1);
				g.score(p1, i);
				assertEquals("now returns Player 2", g.getCurrentPlayer(), p2);
				g.roll(p2);
				g.score(p2, i);
				assertEquals("now returns Player 3", g.getCurrentPlayer(), p3);
				g.roll(p3);
				g.score(p3, i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//System.out.println(g.toString());
		assertTrue("Game finished", g.hasEnded());
	}

	@Test
	public void testPromptPlayerToRoll() {
		g.start();
		assertEquals("Game asks player to start turn", g.promptPlayer(p1), "Press ENTER to Roll!" + Yahtzee.EOL);
	}

	@Test
	public void testPromptPlayerAfterRoll() {
		g.start();
		g.roll(p1);
		assertEquals("Game asks player to choose action", g.promptPlayer(p1), 
				"(1) Select dice to reroll." + Yahtzee.EOL
				+ "(2) Select dice to hold, then reroll the others. " + Yahtzee.EOL 
				+ "(3) Reroll everything." + Yahtzee.EOL 
				+ "(4) Score this round" + Yahtzee.EOL);
	}

	@Test
	public void testNewRound() {
		try {
			g.start();
			assertTrue("New game is in a new round", g.newRound());
//			g.addPlayer(p1);
//			g.addPlayer(p2);
//			g.addPlayer(p3);
			g.roll(p1);
			g.score(p1, 1);
			assertEquals("now returns Player 2", g.getCurrentPlayer(), p2);
			g.roll(p2);
			// g.reroll(p2, new int[]{1,2,3,4,5});
			g.score(p2, 1);
			assertEquals("now returns Player 3", g.getCurrentPlayer(), p3);
			g.roll(p3);
			g.score(p3, 1);
			assertTrue("New Round again", g.newRound());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
