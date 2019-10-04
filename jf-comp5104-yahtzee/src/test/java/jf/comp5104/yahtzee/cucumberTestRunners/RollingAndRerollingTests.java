package jf.comp5104.yahtzee.cucumberTestRunners;

import static org.junit.Assert.*;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.*;
// import jf.comp5104.yahtzee.unitTest.*;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/features", glue="")
public class RollingAndRerollingTests  {

	Game g;
	Player p;
	
	@Given("That a game has started")
	public void that_a_game_has_started() {
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

}
