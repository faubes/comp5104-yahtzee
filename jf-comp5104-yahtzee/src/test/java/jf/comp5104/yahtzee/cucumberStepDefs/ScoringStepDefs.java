package jf.comp5104.yahtzee.cucumberStepDefs;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.exceptions.AlreadyScoredThereException;

import static org.junit.Assert.assertEquals;

public class ScoringStepDefs {
    Player p;
    public ScoringStepDefs() {
    }

    @Given("Player has a roll to score")
    public void playerHasARollToScore() {
        p = new Player();
        p.roll();
    }

    @When("Player scores {int}, {int}, {int}, {int}, {int} into category {int}")
    public void playerScoresRollInCategory(Integer d1, Integer d2, Integer d3, Integer d4, Integer d5, Integer category) throws AlreadyScoredThereException {
        p.getRoll().set(d1, d2, d3, d4, d5);
        p.score(category);
    }

    @Then("Player gets {int} in category {int}")
    public void playerGetsScoreInCategory(Integer score, Integer category) {
        assertEquals(score.intValue(), p.getScore(category));
    }

}
