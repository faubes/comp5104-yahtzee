package jf.comp5104.yahtzee.cucumberTestRunners;

//import static org.junit.Assert.*;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "classpath:testfiles/MacroValidation.feature" }, glue = {
		"jf.comp5104.yahtzee.cucumberTestRunners" }, dryRun = false, monochrome = true, tags = "@macroFilter")

public class RollingAndRerollingTest {

}
