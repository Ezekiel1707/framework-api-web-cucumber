package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.nio.file.Paths;

public class webDriverInstance extends basePageUI {
    public webDriverInstance() {
        super();
    }

    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            try {
                driver.set(createDriver());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create WebDriver", e);
            }
        }
        return driver.get();
    }

    public static Map<String, Object> configureProfile() {
        Map<String, Object> prefs = new HashMap<>();
        Map<String, Object> profile = new HashMap<>();
        Map<String, Object> contentSettings = new HashMap<>();
        contentSettings.put("geolocation", 2);
        contentSettings.put("profile.default_content_setting_values.notifications", 2);
        profile.put("managed_default_content_settings", contentSettings);
        prefs.put("profile", profile);
        prefs.put("download.default_directory", Paths.get(System.getProperty("user.dir"),
                "src", "main", "java", "resources", "Descargas").toString());
        // prefs.put("download.default_directory", System.getProperty("user.dir")
        // + "\\src\\main\\java\\resources\\Descargas\\");
        prefs.put("plugins.always_open_pdf_externally", true);
        return prefs;
    }

    public static WebDriver createDriver() throws IOException {
        // WebDriverManager.chromedriver().setup();
        WebDriver driver;

        if (getGlobalValue("browser").equals("chrome")) {
            Map<String, Object> seleniumOptions = new HashMap<>();
            seleniumOptions.put("build", "CaptchaInSelenium");
            seleniumOptions.put("name", "TCaptchaInSeleniumSample");
            seleniumOptions.put("profile.default_content_setting_values.automatic_downloads", 1);

            ChromeOptions options = new ChromeOptions();
            options.setCapability("selenium:options", seleniumOptions);
            options.setExperimentalOption("prefs", configureProfile());
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-notifications");
            // options.addArguments("--enable-features=ClipboardAllwWithoutSanitization,ClipboardAPI");
            // options.addArguments("--disable-features=BlockInsecurePrivateNetworkRequests");
            // options.addArguments("--window-position=-2400,-2400");

            if (Objects.equals(getGlobalValue("headless"), "yes")) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            // System.setProperty("webdriver.chrome.driver",
            // System.getProperty("user.dir") +
            // "\\src\\main\\java\\resources\\drivers\\chromedriver.exe");
            driver = new ChromeDriver(options);
        } else if (getGlobalValue("browser").equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();

            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("geo.enabled", false); // disables geolocation
            profile.setPreference("permissions.default.desktop-notification", 2); // disable notifications
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("download.default_directory", Paths.get(System.getProperty("user.dir"),
                    "src", "main", "java", "resources", "Descargas").toString());
            profile.setPreference("pdfjs.disabled", true);
            profile.setPreference("plugin.disable_full_page_plugin_for_types", "application/pdf");

            options.setProfile(profile);
            if (Objects.equals(getGlobalValue("headless"), "yes")) {
                options.addArguments("--headless");
                options.addArguments("--window-size=1920,1080");
            }
            System.setProperty("webdriver.gecko.driver",
                    Paths.get(System.getProperty("user.dir"),
                            "src", "main", "java", "resources", "drivers", "geckodriver.exe").toString());
            driver = new FirefoxDriver(options);
        } else if (getGlobalValue("browser").equals("safari")) {
            System.setProperty("webdriver.safari.driver",
                    System.getProperty("user.dir") + "\\src\\main\\java\\resources\\drivers\\safaridriver.exe");
            driver = new SafariDriver();
        } else {
            Map<String, Object> seleniumOptions = new HashMap<>();
            seleniumOptions.put("build", "CaptchaInSelenium");
            seleniumOptions.put("name", "TCaptchaInSeleniumSample");
            seleniumOptions.put("profile.default_content_setting_values.automatic_downloads", 1);

            EdgeOptions options = new EdgeOptions();
            options.setCapability("selenium:options", seleniumOptions);
            options.setExperimentalOption("prefs", configureProfile());
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-notifications");
            // System.setProperty("webdriver.edge.driver",
            // System.getProperty("user.dir") +
            // "\\src\\main\\java\\resources\\drivers\\msedgedriver.exe");
            driver = new EdgeDriver(options);
        }

        if (!Objects.equals(getGlobalValue("headless"), "yes")) {
            driver.manage().window().maximize();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(65));
        // System.out.println(driver.manage().window().getSize());
        return driver;
    }

    public static void cleanupDriver() {
        driver.get().quit();
        driver.remove();
    }
}
