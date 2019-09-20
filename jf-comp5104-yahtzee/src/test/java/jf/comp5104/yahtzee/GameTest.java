package jf.comp5104.yahtzee;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import jf.comp5104.yahtzee.net.YahtzeeServer;

import org.junit.*;

public class GameTest {

	private Game g;
	private Player p1;
	private Player p2;
	private Player p3;
	private YahtzeeServer server;
	
	@Before
	public void setUp() {
		try {
			server = new YahtzeeServer(3333, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g = new Game("Game 1", server);
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
		//TODO: actually test if the returned player has best score
	}
	
	@Test
	public void testGetCurrentPlayer() {
		assertEquals("Get Current Player returns Player 1", g.getCurrentPlayer() == p1);
	}
	
}
