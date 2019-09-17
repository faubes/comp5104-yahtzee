package jf.comp5104.yahtzee;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.*;

public class GameTest {

	private Game g;
	private Player p1;
	private Player p2;
	private Player p3;
	
	@Before
	public void setUp() {
		g = new Game();
		g.addPlayer(p1);
		g.addPlayer(p2);
		g.addPlayer(p3);		
	}
	
	@Test
	public void testGetRound() {
		assertTrue("New game in round 0 until first roll", g.getRound() == 0);
	}
	
	@Test
	public Player testGetWinner() {
		assertTrue("GetWinner returns a player", g.getWinner() instanceof Player);
	}
	
	@Test
	public Player getCurrentPlayer() {
		assertEquals("Get Current Player returns Player 1", g.getCurrentPlayer() == p1);
	}

}
