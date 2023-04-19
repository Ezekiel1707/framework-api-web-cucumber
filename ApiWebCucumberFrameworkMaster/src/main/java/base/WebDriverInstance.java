package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WebDriverInstance extends BasePageUI{
    public WebDriverInstance() {
        super();
    }

    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            try {
                driver.set(createDriver());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return driver.get();
    }

    public static Map<String, Object> configureProfile(){
        Map<String, Object> prefs = new HashMap<String, Object>();
        Map<String, Object> profile = new HashMap<String, Object>();
        Map<String, Object> contentSettings = new HashMap<String, Object>();
        contentSettings.put("geolocation", 2);
        profile.put("managed_default_content_settings", contentSettings);
        prefs.put("profile", profile);
        prefs.put("download.default_directory", System.getProperty("user.dir")
                + "\\src\\main\\java\\resources\\Descargas\\");
        return prefs;
    }

    public static WebDriver createDriver() throws IOException {
        WebDriver driver;

        if (getGlobalValue("browser").equals("chrome")) {
            Map<String, Object> seleniumOptions = new HashMap<>();
            seleniumOptions.put("build", "CaptchaInSelenium");
            seleniumOptions.put("name", "TCaptchaInSeleniumSample");
            seleniumOptions.put("profile.default_content_setting_values.automatic_downloads",1);

            ChromeOptions options = new ChromeOptions();
            options.setCapability("selenium:options", seleniumOptions);
            options.setExperimentalOption("prefs", configureProfile());
            options.addArguments("--remote-allow-origins=*");
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "\\src\\main\\java\\resources\\drivers\\chromedriver.exe");
            driver = new ChromeDriver(options);
        } else if (getGlobalValue("browser").equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.setCapability("prefs", configureProfile());
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir") + "\\src\\main\\java\\resources\\drivers\\geckodriver.exe");
            driver = new FirefoxDriver(options);
        } else if (getGlobalValue("browser").equals("safari")) {
            System.setProperty("webdriver.safari.driver",
                    System.getProperty("user.dir") + "\\src\\main\\java\\resources\\drivers\\safaridriver.exe");
            driver = new SafariDriver();
        }
        else {
            EdgeOptions options = new EdgeOptions();
            options.setCapability("prefs", configureProfile());
            System.setProperty("webdriver.edge.driver",
                    System.getProperty("user.dir") + "\\src\\main\\java\\resources\\drivers\\msedgedriver.exe");
            driver = new EdgeDriver(options);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        return driver;
    }
    public static void cleanupDriver() {
        driver.get().quit();
        driver.remove();
    }
}

