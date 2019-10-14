package jf.comp5104.yahtzee.cucumberStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.Game;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.Roll;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameLogicStepDefs {
    private Game g;
    private Player p;

    @Given("A game has started")
    public void gameHasStarted() {
        p = new Player();
        g = new Game();
        g.addPlayer(p);
        g.start();
        assertTrue(g.hasStarted());
    }

    @Given("Player has not yet rolled")
    public void playerHasNotYetRolled() {
        assertTrue(g.isFirstRoll());
    }

    @When("Player rolls")
    public void playerRolls() {
        g.roll(p);
    }

    @Then("Player gets a new set of 5 dice")
    public void playerGetsNewSet5Dice()
    {
        assertNotNull(p.getRoll());
        assertEquals(5, p.getRoll().size());
    }

    @Given("Player has rolled once")
    public void playerHasRolledOnce() {
        g.roll(p);
    }


    @Given("Player has rolled twice")
    public void thePlayerHasRolledOnce() {
        g.roll(p);
        g.reroll(p);
    }

    @Given("It is player's turn")
    public void itIsPlayerSTurn() {
        assertEquals(g.getCurrentPlayer(), p);
    }

    @Then("Player may reroll")
    public void playerMayReroll() {
        g.reroll(p);
    }

    @Given("Player has rolled thrice")
    public void playerHasRolledThrice() {
        g.roll(p);
        g.reroll(p);
        g.reroll(p);
    }

    @Then("Player cannot reroll")
    public void playerCannotReroll() throws Exception {
        try {
            g.reroll(p);
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalStateException);
            return;
        }
        Assert.fail();
    }
}