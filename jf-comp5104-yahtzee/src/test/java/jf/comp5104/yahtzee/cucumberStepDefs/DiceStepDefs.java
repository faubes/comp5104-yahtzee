package jf.comp5104.yahtzee.cucumberStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.Roll;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DiceStepDefs {

    Roll r1, r2;

    @When("One roll {int}, {int}, {int}, {int}, {int}")
    public void oneRoll(Integer int1, Integer int2, Integer int3, Integer int4, Integer int5) {
        r1 = new Roll(int1, int2, int3, int4, int5);
    }

    @When("Another Roll {int} {int}, {int}, {int}, {int}")
    public void anotherRoll(Integer int1, Integer int2, Integer int3, Integer int4, Integer int5) {
        r2 = new Roll(int1, int2, int3, int4, int5);
    }

    @Then("They are equivalent")
    public void they_are_equivalent() {
        assertEquals(r1, r2);
    }

}
