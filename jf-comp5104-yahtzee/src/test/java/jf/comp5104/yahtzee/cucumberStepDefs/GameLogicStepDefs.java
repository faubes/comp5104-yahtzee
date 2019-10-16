package jf.comp5104.yahtzee.cucumberStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.Game;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.Roll;
import jf.comp5104.yahtzee.exceptions.AlreadyScoredThereException;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameLogicStepDefs {
    private Game g;
    private Player p;

    @Given("A game with {int} players has started")
    public void gameHasStarted(int numPlayers) {
        g = new Game();
        for (int i = 0; i < numPlayers; i++ ) {
            p = new Player();
            g.addPlayer(p);
        }
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
        p = g.getCurrentPlayer();
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


    @When("Every player has had a turn")
    public void everyPlayerHasHadATurn() throws Exception {
        for (int i = 0; i < g.getNumberOfPlayers(); i++) {
            p = g.getCurrentPlayer();
            g.roll(p);
            g.score(p, 12);
        }
    }


    @Then("Round {int} begins")
    public void roundBegins(int round) {
        assertEquals(round, g.getRound());
    }

    @And("Player can score")
    public void playerCanScore() throws AlreadyScoredThereException {
        p.score(1);
    }

    @Then("Player may reroll the first and second die")
    public void playerMayRerollTheFirstAndSecondDie() {
        p.reroll(1, 2);
    }

    @When("Player ends the game")
    public void playerEndsTheGame() {
        g.stop();
    }

    @Then("Game is over")
    public void gameIsOver() {
        assertTrue(g.hasEnded());
    }
}