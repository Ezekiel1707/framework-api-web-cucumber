package pageObjectsUI;

import base.BasePageUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.io.IOException;

public class ejemploPageObjects extends BasePageUI {
    public WebDriver driver;

    By name = By.cssSelector("ejemplo");
    By list = By.xpath("list");

    public ejemploPageObjects() throws IOException {
        super();
    }

    public WebElement getName() throws IOException {
        this.driver = getDriver();
        return  driver.findElement(name);
    }
    public List<WebElement> getList() throws IOException {
        this.driver = getDriver();
        return  driver.findElements(list);
    }
}
