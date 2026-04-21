package base;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
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
import java.util.stream.Collectors;

public class basePageUI {

    private static String screeShotDestinationPath;
    private static Properties globalProperties;

    public static String getGlobalValue(String key) throws IOException {
        if (globalProperties == null) {
            globalProperties = new Properties();
            Path propPath = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "resources",
                    "global.properties");
            try (FileInputStream data = new FileInputStream(propPath.toFile())) {
                globalProperties.load(data);
            }
        }
        return globalProperties.getProperty(key);
    }

    public static WebDriver getDriver() {
        return webDriverInstance.getDriver();
    }

    public static String takeSnapShot(String name) throws IOException {
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String destFile = Paths
                .get(System.getProperty("user.dir"), "test-output", "Reports", "screenshots", timestamp() + ".png")
                .toString();
        screeShotDestinationPath = destFile;
        FileUtils.copyFile(srcFile, new File(destFile));
        return destFile;
    }

    public static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
    }

    public static String getScreenshotDestination() {
        return screeShotDestinationPath;
    }

    public static void waitForElementVisible(WebElement element, Duration i) {
        WebDriverWait wait = new WebDriverWait(getDriver(), i);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForElementClickable(WebElement element, Duration i) {
        WebDriverWait wait = new WebDriverWait(getDriver(), i);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitElement(String locator, Duration d) throws IOException {
        int retries_max = Integer.parseInt(getGlobalValue("retries"));
        for (int retries = 0; retries <= retries_max; retries++) {
            try {
                WebDriverWait wait = new WebDriverWait(getDriver(), d);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
                return;
            } catch (TimeoutException | StaleElementReferenceException e) {
                if (retries == retries_max) {
                    throw new RuntimeException("Element not visible after retries: " + locator, e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void waitElement_csslocator(String csslocator, Duration d) throws InterruptedException, IOException {
        int retries_max = Integer.parseInt(getGlobalValue("retries"));
        for (int retries = 0; retries <= retries_max; retries++) {
            try {
                WebDriverWait wait = new WebDriverWait(getDriver(), d);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(csslocator)));
                return;
            } catch (TimeoutException | StaleElementReferenceException e) {
                if (retries == retries_max) {
                    throw new RuntimeException("Element not visible after retries: " + csslocator, e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void sendText(WebElement element, String value) {
        // This method only work on Windows, for MacOS the combination of keys to select
        // all text is different (Command + A)

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

    public static WebElement getFirstSelectedOption(WebElement element) {
        Select select = new Select(element);
        return select.getFirstSelectedOption();
    }

    public static void selectByIndex(WebElement element, int option) {
        Select select = new Select(element);
        select.selectByIndex(option);
    }

    public static void scrollToElement(WebElement element) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoViewIfNeeded();", element);
        Thread.sleep(2000);
    }

    public static void waitForElementInvisible(WebElement element, Duration timeout) {
        new WebDriverWait(getDriver(), timeout)
                .until(ExpectedConditions.invisibilityOf(element));
    }

    public static void waitForElementAvailable(WebElement element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
        ExpectedCondition<Boolean> elementIsAvailable = arg0 -> {
            try {
                element.isDisplayed();
                return true;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        };
        wait.until(elementIsAvailable);
    }

    public static void waitForElementEnable(WebElement element, Duration i) {
        WebDriverWait wait = new WebDriverWait(getDriver(), i);
        ExpectedCondition<Boolean> elementIsEnable = arg0 -> {
            try {
                element.isEnabled();
                return true;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        };
        wait.until(elementIsEnable);
    }

    public static void openNewTab() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.open();");
    }

    public static String getText(WebElement element) {
        return element.getText();
    }

    public static String getText(String locator) throws NoSuchElementException {
        WebElement element = getDriver().findElement(By.xpath(locator));
        return element.getText();
    }

    public static String getAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    public static String getDomAttribute(WebElement element, String attribute) {
        return element.getDomAttribute(attribute);
    }

    public static String getDomProperty(WebElement element, String attribute) {
        return element.getDomProperty(attribute);
    }

    public static void validateResults(String actual, String expected) {
        Assert.assertEquals(actual, expected);
    }

    public static void isNotEmpty(String text) {
        Assert.assertFalse(text.isEmpty());
    }

    public static Boolean elementIsPresent(String locator) throws IOException {
        Duration originalWait = getDriver().manage().timeouts().getImplicitWaitTimeout();
        try {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            return !getDriver().findElements(By.xpath(locator)).isEmpty();
        } finally {
            getDriver().manage().timeouts().implicitlyWait(originalWait);
        }
    }

    public static Boolean cssElementIsPresent(String csslocator) {

        Duration originalWait = getDriver().manage().timeouts().getImplicitWaitTimeout();
        try {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
            return !getDriver().findElements(By.cssSelector(csslocator)).isEmpty();
        } finally {
            getDriver().manage().timeouts().implicitlyWait(originalWait);
        }
    }

    public static Boolean elementIsPresent(WebElement element) throws IOException {
        Duration originalWait = getDriver().manage().timeouts().getImplicitWaitTimeout();
        try {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        } finally {
            getDriver().manage().timeouts().implicitlyWait(originalWait);
        }
    }

    public static void click(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        ExpectedCondition<Boolean> elementIsAvailable = arg0 -> {
            try {
                element.click();
                return true;
            } catch (ElementClickInterceptedException | NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        };
        wait.until(elementIsAvailable);
    }

    public static void click2(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        ExpectedCondition<Boolean> elementToBeClickable = arg0 -> {
            try {
                element.click();
                return true;
            } catch (ElementClickInterceptedException | NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        };
        wait.until(elementToBeClickable);
    }

    public static void click(String locator) throws NoSuchElementException {
        WebElement element = getDriver().findElement(By.xpath(locator));
        element.click();
    }

    public static void clickOutside() {
        Actions action = new Actions(getDriver());
        action.moveByOffset(5, 5).click().build().perform();
    }

    public static void clickOutside(WebElement element) {
        Actions action = new Actions(getDriver());
        action.moveToElement(element).click().build().perform();
    }

    public static void clickToWebElementAction(WebElement element) {

        Point point = element.getLocation();
        int x = point.getX();
        int y = point.getY();
        Actions action = new Actions(getDriver());
        action.moveByOffset(x, y).click().build().perform();
        // To click in a WebElement went the locator is not working properly
    }

    public static void moveToWebElementAction(WebElement element) throws IOException {
        try {
            Actions action = new Actions(getDriver());
            action.moveToElement(element).build().perform();
        } catch (Exception e) {
            executeJavaScript("arguments[0].scrollIntoView(true);", element);
        }
    }

    public static List<WebElement> getAllOptions(WebElement element) {
        Select select = new Select(element);
        return select.getOptions();
    }

    public static Boolean compareList(List<WebElement> actual, List<String> expected) {
        List<String> actual_list = new ArrayList<>();
        boolean compare = false;
        for (WebElement element : actual) {
            // System.out.println(actual.get(i).getText());
            actual_list.add(element.getText());
        }
        if (new HashSet<>(expected).containsAll(actual_list) && expected.size() == actual_list.size()) {
            compare = true;
        }
        return compare;
    }

    public static void clickJS(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    public static void executeJavaScript(String script) throws IOException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(script);
    }

    public static void executeJavaScript(String script, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(script, element);
    }

    public static void goToTab(String name) {
        ArrayList<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
        if (name.equals("default")) {
            getDriver().close();
            getDriver().switchTo().window(tabs.get(0));
        } else {
            getDriver().switchTo().window(tabs.get(1));
        }
    }

    public static boolean checkClickable(WebElement element, Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static void refreshPage() {
        getDriver().navigate().refresh();
    }

    public static void refreshPage(int milliseconds) throws InterruptedException {
        getDriver().navigate().refresh();
        Thread.sleep(milliseconds);
    }

    public static boolean isFileDownloaded(String path, String fileName, int iterations) {
        Path filePath = Paths.get(path, fileName);
        for (int waitForDownload = 0; waitForDownload < iterations; waitForDownload++) {
            if (Files.exists(filePath)) {
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted state
                return false;
            }
        }
        return false;
    }

    public static void deleteFile(String path, String fileName) throws IOException {
        Files.delete(Paths.get(path, fileName));
    }

    public static void uploadFileWithRobot(String path) {
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

    public static void uploadFile(WebElement element, String uploadFilePath) {
        element.sendKeys(uploadFilePath);
    }

    public static Set<String> filesListBeforeDownload(String downloadDir) {
        File dir = new File(downloadDir);

        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("Download directory is invalid: " + downloadDir);
        }

        File[] initialFiles = dir.listFiles();

        Set<String> before;
        if (initialFiles == null) {
            before = new HashSet<>();
        } else {
            before = Arrays.stream(initialFiles)
                    .map(File::getName)
                    .collect(Collectors.toSet());
        }
        return before;
    }

    public String getNewDownloadedFileName(String downloadDir, Set<String> before, int timeoutSeconds)
            throws InterruptedException {

        File dir = new File(downloadDir);

        int waited = 0;

        while (waited < timeoutSeconds) {
            Thread.sleep(1000);
            waited++;

            File[] files = dir.listFiles();
            if (files == null)
                continue;

            for (File file : files) {
                String name = file.getName();

                // Ignore Chrome temp files
                if (name.endsWith(".crdownload"))
                    continue;

                if (!before.contains(name)) {

                    // Wait until file size stabilizes
                    if (isFileStable(file, 3, 500)) {
                        return name;
                    }
                }
            }
        }

        throw new RuntimeException("No completed file downloaded within timeout");
    }

    private static boolean isFileStable(File file, int checks, int milliseconds) throws InterruptedException {
        long previousSize = -1;

        for (int i = 0; i < checks; i++) {
            long currentSize = file.length();

            if (currentSize == previousSize) {
                return true;
            }

            previousSize = currentSize;
            Thread.sleep(milliseconds);
        }

        return false;
    }

}
