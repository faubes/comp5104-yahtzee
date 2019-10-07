package jf.comp5104.yahtzee.cucumberStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.Game;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.Roll;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RollingAndRerollingStepDefs {
    Game g;
    Player p;
    private List<Integer> idx = new ArrayList<>();

    @Given("You start a game")
    public void youHaveAPlayer() {
        p = new Player();
        g = new Game();
        g.addPlayer(p);
        g.start();
    }

    @Given("A game has started")
    public void a_game_has_started() {
        assertTrue(g.hasStarted());
    }


    @Given("You have not yet rolled")
    public void you_have_not_yet_rolled() {
        assertTrue(g.isFirstRoll());
    }


    @When("You roll")
    public void you_roll() {
        g.roll(p);
    }

    @Then("You get a new set of dice")
    public void youGetANewSetOfDice() {
        assertTrue(p.getRoll() != null);
    }

    @Given("You have rolled once")
    public void you_have_rolled_once() {
        g.roll(p);
    }

    @Then("You get a new set of dice values")
    public void youGetANewSetOfDiceValues() {
        assertNotNull(g.getCurrentPlayer().getRoll());
    }

    @Given("The player has rolled once")
    public void thePlayerHasRolledOnce() {
        g.roll(p);
    }

    @When("You hold dice {int}, {int}, {int}")
    public void youHoldDice(int arg0, int arg1, int arg2) {
        idx.clear();
        idx.add(arg0);
        idx.add(arg1);
        idx.add(arg2);
    }

    @Then("You hold {int}, {int}, {int} and reroll dice {int}, {int}")
    public void youHoldAndRerollDice(int arg0, int arg1, int arg2, int arg3, int arg4) {
        int[] actualRerollIndex = Roll.indexComplement(arg0, arg1, arg2);
        g.reroll(p, actualRerollIndex);
        assertEquals(actualRerollIndex[0], arg3);
        assertEquals(actualRerollIndex[1], arg4);
    }

    @Given("The player has rolled twice")
    public void thePlayerHasRolledTwice() {
        g.roll(p);
        g.reroll(p);
    }

    @When("You reroll {int}, {int}, {int}")
    public void youReroll(int arg0, int arg1, int arg2) {
        // do nothing?
    }
}