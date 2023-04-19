package cucumber.Options;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

//@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/featuresAPI", glue =
        "stepsDefinitionsAPI", tags = "@TAG", plugin =
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")

public class TestRunnerAPI extends AbstractTestNGCucumberTests {

}