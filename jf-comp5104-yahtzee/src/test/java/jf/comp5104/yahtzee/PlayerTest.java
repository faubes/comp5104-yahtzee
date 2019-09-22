package jf.comp5104.yahtzee;

import org.junit.*;

import static org.junit.Assert.*;


public class PlayerTest {

	private Player p;
	
	@Before
	public void setUp() {
		p = new Player();
	}
	
	@After
	public void tearDown() {
		p = null; // destroy player
	}

	@Test
	public void testPlayerDefaultConstructor() {
		assertTrue("Default player name", p.getName().startsWith("New Player"));
	}

	
	@Test
	public void testPlayerConstructorWithName() {
		p = new Player("Joe");
		assertEquals("Create new player named Joe", "Joe", p.getName());
	}
	
	
	@Test
	public void testPlayerIsConnected() {
		assertFalse("Default player not connected", p.isConnected());
		p.setConnected(true);
		assertTrue("Player now connected", p.isConnected());
	}
	
	@Test
	public void testPlayerHasScoresheet() {
		assertTrue("Non-zero score", p.getScore() >= 0);
	}
	
	@Test
	public void testPlayerHasToString() {
		assertTrue("Some kind of String", p.toString() instanceof String);
	}
	
	@Test
	public void testPlayerCanRoll() {
		p.roll();
		assertTrue("Player can roll the dice", p.getRoll() instanceof Roll);
	}
	
	@Test
	public void testPlayerCanReroll() {
		p.roll();
		p.reroll(1, 2, 3);
		p.reroll(1);
		assertTrue("Player can reroll the dice twice", p.getRoll() instanceof Roll);
	}
	
	@Test
	public void testPlayerCanScore() {
		p.roll();
		try {
		p.score(12);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue("Player records a score in Chance", p.getScore() > 0);
	}
}
