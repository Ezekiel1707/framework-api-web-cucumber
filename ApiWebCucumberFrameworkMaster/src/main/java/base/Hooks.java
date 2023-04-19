package base;

import java.io.IOException;
import org.testng.annotations.*;

public class Hooks extends BasePageUI {

    public Hooks() {
        super();

    }

    @BeforeTest
    public void setup() throws IOException {
        System.out.println("setup");
        getDriver().get(getGlobalValue("url"));
    }

    @AfterTest
    public void tearDown() {
        System.out.println("after");
        WebDriverInstance.cleanupDriver();
    }

}
