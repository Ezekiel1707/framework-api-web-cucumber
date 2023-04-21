package pageObjectsUI;

import base.BasePageUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;

public class StoreLoginPage extends BasePageUI {

    public WebDriver driver;

    By email = By.cssSelector("section input[name='email']");
    By password = By.cssSelector("[name='password']");
    By loginBtn = By.cssSelector("[data-link-action='sign-in']");
    By headerTxt = By.xpath("//section[@id='main']//h1[1]");
    By logOutBtn = By.cssSelector(".user-info [rel='nofollow']:nth-of-type(1)");
    public StoreLoginPage() throws IOException {
        super();
    }
    public WebElement getLogOutBtn() throws IOException {
        this.driver = getDriver();
        return driver.findElement(logOutBtn);
    }
    public WebElement getHeaderTxt() throws IOException {
        this.driver = getDriver();
        return driver.findElement(headerTxt);
    }
    public WebElement getEmail() throws IOException {
        this.driver = getDriver();
        return driver.findElement(email);
    }

    public WebElement getPassword() throws IOException {
        this.driver = getDriver();
        return driver.findElement(password);
    }

    public WebElement getLoginBtn() throws IOException {
        this.driver = getDriver();
        return driver.findElement(loginBtn);
    }
}