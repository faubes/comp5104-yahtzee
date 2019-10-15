package jf.comp5104.yahtzee.cucumberTestRunners;

//import static org.junit.Assert.*;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources", glue = "jf.comp5104.yahtzee.cucumberStepDefs")

public class ScoringTest {
}
