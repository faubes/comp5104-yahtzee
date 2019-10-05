package jf.comp5104.yahtzee.cucumberStepDefs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.Game;
import jf.comp5104.yahtzee.Player;

public class RollingAndRerollingStepDefs {
	Game g;
	Player p;

	@Given("A game has started")
	public void a_game_has_started() {
		g = new Game();
		p = new Player();
		g.addPlayer(p);
		g.start();
	}

	@Given("You are the current player")
	public void you_are_the_current_player() {
		// kinda pointless, but ok.
		assertEquals(p, g.getCurrentPlayer());
	}

	@Given("You have not yet rolled")
	public void you_have_not_yet_rolled() {
		// also, brand new game, new player but
		assertTrue(g.isFirstRoll());
	}

	@When("You roll")
	public void you_roll() {
		p.roll();
	}

	@Then("You get a new set of {int} dice")
	public void you_get_a_new_set_of_dice(Integer int1) {
		p.getRoll();
	}

	@Given("You have rolled at least once")
	public void you_have_rolled_at_least_once() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Given("You have not already rerolled twice")
	public void you_have_not_already_rerolled_twice() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@When("You hold some dice")
	public void you_hold_some_dice() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("You reroll the others")
	public void you_reroll_the_others() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}
}
