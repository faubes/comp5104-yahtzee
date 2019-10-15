package jf.comp5104.yahtzee.cucumberStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jf.comp5104.yahtzee.Roll;

import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class DiceStepDefs {

    private Roll r1, r2;

    @Given("One roll {int}, {int}, {int}, {int}, {int}")
    public void oneRoll(Integer int1, Integer int2, Integer int3, Integer int4, Integer int5) {
        r1 = new Roll(int1, int2, int3, int4, int5);
    }

    @Given("Another roll {int}, {int}, {int}, {int}, {int}")
    public void anotherRoll(Integer int1, Integer int2, Integer int3, Integer int4, Integer int5) {
        r2 = new Roll(int1, int2, int3, int4, int5);
    }

    @Then("Roll one and roll two are equivalent")
    public void rollOneAndRollTwoAreEquivalent() {
//        System.out.println(r1.equals(r2));
        assertTrue(r1.equals(r2));
    }

    @Then("There are {int} {int}'s")
    public void numberOfMultiples(int num, int val) {
        assertEquals(num, r1.getMultiple(val));
    }
}
