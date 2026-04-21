package stepsDefinitionsUI;

import base.basePageUI;
import base.webDriverInstance;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;

public class cucumberHooks extends basePageUI {
    public static Scenario scenarioFinal;

    @Before
    public void setup(Scenario scenario) throws IOException {
        getDriver().get(getGlobalValue("url"));
        scenarioFinal = scenario;
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) throws IOException {

        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        webDriverInstance.cleanupDriver();
    }

    @AfterStep("@Screenshot")
    public static void screenshot(Scenario scenario) throws IOException {

        byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "image");
    }

}
