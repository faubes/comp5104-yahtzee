package jf.comp5104.yahtzee;

import org.junit.*;

import static org.junit.Assert.*;

//import java.util.ArrayList;
//import java.util.List;


public class PlayerTest {

	private Player p;
	//private List<Player> l;
	
	@Before
	public void setUp() {
		p = new Player();
		//List<Player> l = new ArrayList<Player>();
	}
	
	@After
	public void tearDown() {
		//l = null; // destroy list
	}

	@Test
	public void testPlayerDefaultConstructor() {
		assertEquals("Default player name", "New Player", p.getName());
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
	
}
