package jf.comp5104.yahtzee;

import org.junit.Test;

import static org.junit.Assert.*;


public class PlayerTest {

	@Test
	public void NewPlayerTest() {
		Player p = new Player("Joe");
		assertEquals("Create new player named Joe", "Joe", p.getName());
	}
}
