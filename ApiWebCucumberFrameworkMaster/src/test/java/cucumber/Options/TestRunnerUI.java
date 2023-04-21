package cucumber.Options;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/featuresUI", glue =
        "stepsDefinitionsUI", tags = "@Login_Store", plugin =
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")

public class TestRunnerUI extends AbstractTestNGCucumberTests {

}
