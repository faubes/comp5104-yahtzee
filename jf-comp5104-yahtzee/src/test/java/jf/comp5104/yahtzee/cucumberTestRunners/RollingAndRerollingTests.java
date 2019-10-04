package jf.comp5104.yahtzee.cucumberTestRunners;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.*;

public class RollingAndRerollingTests {

	Game g;
	Player p;
	
	
	@Given("That a game has started")
	public void that_a_game_has_started() {
		g.addPlayer(p);
		g.start();
	}

	@Given("You are the current player")
	public void you_are_the_current_player() {
		
	}

	@Given("You have not yet rolled")
	public void you_have_not_yet_rolled() {
		
	}

	@When("You roll")
	public void you_roll() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("You get a new set of {int} dice")
	public void you_get_a_new_set_of_dice(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}


}
