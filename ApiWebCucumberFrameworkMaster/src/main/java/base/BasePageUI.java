package base;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.List;

public class BasePageUI {
    public static String screeShotDestinationPath;

    public static String getGlobalValue(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream data = new FileInputStream(
                System.getProperty("user.dir") + "\\src\\main\\java\\resources\\global.properties");
        prop.load(data);
        return prop.getProperty(key);
    }

    public static WebDriver getDriver() throws IOException {
        return WebDriverInstance.getDriver();
    }

    public static String takeSnapShot(String name) throws IOException {
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String destFile = System.getProperty("user.dir") + "\\test-output\\Reports\\screenshots\\" + timestamp() + ".png";
        screeShotDestinationPath = destFile;

        try {
            FileUtils.copyFile(srcFile, new File(destFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
    }

    public static String getScreenshotDestination() {
        return screeShotDestinationPath;
    }

    public static void waitForElementVisible(WebElement element, Duration i) throws IOException {
        WebDriverWait wait = new WebDriverWait(getDriver(), i);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitElement(String locator, Duration d) throws IOException, InterruptedException {
        try {
            int retries_max = Integer.parseInt(getGlobalValue("retries"));
            WebDriverWait wait = new WebDriverWait(getDriver(), d);
            boolean status = getDriver().findElement(By.xpath(locator)).isDisplayed();

            for (int retries = 0; retries <= retries_max; retries++) {
                if (Objects.equals(status, false)) {
                    Thread.sleep(2000);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
                } else
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void waitElement_csslocator(String csslocator, Duration d) throws InterruptedException {
        try {
            int retries_max = Integer.parseInt(getGlobalValue("retries"));
            WebDriverWait wait = new WebDriverWait(getDriver(), d);
            boolean status = getDriver().findElement(By.cssSelector(csslocator)).isDisplayed();

            for (int retries = 0; retries <= retries_max; retries++) {
                if (Objects.equals(status, false)) {
                    Thread.sleep(2000);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(csslocator)));
                } else
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendText(WebElement element, String value) {
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(value);
    }

    public static void selectByValue(WebElement element, String option) {
        Select select = new Select(element);
        select.selectByValue(option);
    }

    public static void selectByText(WebElement element, String option) {
        Select select = new Select(element);
        select.selectByVisibleText(option);
    }

    public static void selectByIndex(WebElement element, int option) {
        Select select = new Select(element);
        select.selectByIndex(option);
    }
    public static void scrollToElement(WebElement element) throws IOException, InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoViewIfNeeded();", element);
        Thread.sleep(2000);
    }

    public static void waitForElementInvisible(WebElement element, Duration i) throws IOException {
        WebDriverWait wait = new WebDriverWait(getDriver(), i);
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public static void openNewTab() throws IOException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.open();");
    }

    public static String getText(WebElement element) {
        return element.getText();
    }

    public static String getText(String locator) throws IOException, InterruptedException {
        WebElement element = getDriver().findElement(By.xpath(locator));
        return element.getText();
    }

    public static String getAttribute(WebElement element, String attribute) throws IOException {
        return element.getAttribute(attribute);
    }

    public void validateResults(String actual, String expected) {
        Assert.assertEquals(actual, expected);
    }

    public static void isNotEmpty(String text) {
        Assert.assertFalse(text.isEmpty());
    }

    public static Boolean elementIsPresent(String locator) throws IOException {

        try {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            getDriver().findElement(By.xpath(locator));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        finally {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        }
        return true;
    }
    public static Boolean cssElementIsPresent(String csslocator) throws IOException {

        try {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            getDriver().findElement(By.cssSelector(csslocator));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        finally {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        }
        return true;
    }
    public static Boolean elementIsPresent(WebElement element) {
        try {
            waitForElementVisible(element, Duration.ofSeconds(15));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static void click(WebElement element) throws IOException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        ExpectedCondition<Boolean> elementIsAvailable = arg0 -> {
            try {
                element.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        };
        wait.until(elementIsAvailable);
    }

    public static void click2(WebElement element) throws IOException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        ExpectedCondition<Boolean> elementToBeClickable = arg0 -> {
            try {
                element.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        };
        wait.until(elementToBeClickable);
    }

    public static void click(String locator) throws IOException {
        WebElement element = getDriver().findElement(By.xpath(locator));
        element.click();
    }

    public void clickOutside() throws IOException {
        Actions action = new Actions(getDriver());
        action.moveByOffset(5, 5).click().build().perform();
    }
    public void clickOutside(WebElement element) throws IOException {
        Actions action = new Actions(getDriver());
        action.moveToElement(element).click().build().perform();
    }
    public void clickToWebElementAction(WebElement element) throws IOException {

        Point point = element.getLocation();
        int x = point.getX();
        int y = point.getY();
        Actions action = new Actions(getDriver());
        action.moveByOffset(x, y).click().build().perform();
        // To click in a WebElement went the locator is not working properly
    }
    public void moveToWebElementAction(WebElement element) throws IOException {

        Actions action = new Actions(getDriver());
        action.moveToElement(element).build().perform();

    }

    public static List<WebElement> getAllOptions(WebElement element) {
        Select select = new Select(element);
        return select.getOptions();
    }

    public static Boolean compareList(List<WebElement> actual, List<String> expected) {
        List<String> actual_list = new ArrayList<>();

        boolean compare = false;
        for (int i = 1; i < actual.size(); i++) {
            System.out.println(actual.get(i).getText());
            actual_list.add(actual.get(i).getText());
        }

        if (new HashSet<>(expected).containsAll(actual_list) && expected.size() == actual_list.size()) {
            compare = true;
        }
        return compare;
    }

    public static void clickJS(WebElement element) throws IOException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    public static void executeJavaScript(String script) throws IOException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(script);
    }

    public static void executeJavaScript(String script, WebElement element) throws IOException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(script, element);
    }

    public static void goToTab(String name) throws IOException {
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        if(name.equals("default")){
            getDriver().close();
            getDriver().switchTo().window(tabs.get(0));
        } else {
            getDriver().switchTo().window(tabs.get(1));
        }
    }

    public static void checkClikable(WebElement element) throws IOException {
        ExpectedCondition<Boolean> elementToBeClickable = arg0 -> {
            try {
                element.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        };
    }

    public static void refreshPage() throws IOException, InterruptedException {
        getDriver().navigate().refresh();
        Thread.sleep(3000);
    }

    public static boolean isFileDownloaded(String path, String fileName, int iterations) throws Exception{
        boolean check = false;
        //String path = System.getProperty("user.dir") + "\\src\\main\\java\\resources\\Descargas\\";
        Path dir = Paths.get(path);
        for(int waitForDownload=0;waitForDownload<iterations;waitForDownload++){
            File[] dir_contents = dir.toFile().listFiles();
            for (int i = 0; i < Objects.requireNonNull(dir_contents).length; i++) {
                if (Objects.equals(dir_contents[i].getName(),fileName)){
                    //Files.delete(Path.of(path + fileName));
                    return check=true;
                }
            }
            Thread.sleep(1000);
        }
        return check;
    }

    public static void deleteFile(String path, String fileName) throws Exception{
        try {
            Files.delete(Path.of(path + fileName));;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileWithRobot (String path) {
        StringSelection str = new StringSelection(path);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
        try {
            Robot rb = new Robot();
            rb.delay(500);
            rb.keyPress(KeyEvent.VK_CONTROL);
            rb.keyPress(KeyEvent.VK_V);
            rb.keyRelease(KeyEvent.VK_V);
            rb.keyRelease(KeyEvent.VK_CONTROL);
            rb.keyPress(KeyEvent.VK_ENTER);
            rb.delay(250);
            rb.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }
}
