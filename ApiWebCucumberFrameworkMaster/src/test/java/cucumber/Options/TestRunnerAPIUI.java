package cucumber.Options;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/featuresAPIUI", glue =
        {"stepsDefinitionsUI","stepsDefinitions"}, tags = "@TAG", plugin =
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")

public class TestRunnerAPIUI extends AbstractTestNGCucumberTests {

}