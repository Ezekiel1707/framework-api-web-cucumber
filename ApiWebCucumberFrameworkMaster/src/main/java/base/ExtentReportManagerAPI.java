package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners(resources.ListenersAPI.class)
public class ExtentReportManagerAPI extends BasePageAPI{
    public static ExtentReports extentReport;
    public static String extentReportPrefix;
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public ExtentReportManagerAPI() throws IOException {
        super();
    }

    public static ExtentReports getReport() {
        if (extentReport == null) {
            setupExtentReports("Report");
        }
        return extentReport;
    }

    public static ExtentReports setupExtentReports(String testName) {
        extentReport = new ExtentReports();
        String ExtentSparkReporterFolder = System.getProperty("user.dir") + "/test-output/Reports/"+extentReportsPrefix_Name(testName)+"/" +
                extentReportsPrefix_Name(testName);
        ExtentSparkReporter spark = new ExtentSparkReporter(ExtentSparkReporterFolder+ ".html");
        extentReport.attachReporter(spark);

        extentReport.setSystemInfo("Tester", "Prueba");
        spark.config().setReportName("Test");
        spark.config().setTheme(Theme.DARK);
        return extentReport;
    }

    public static String extentReportsPrefix_Name(String testName) {
        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        extentReportPrefix = testName + "_" + date;
        return extentReportPrefix;
    }

    public static void flushReport() {
        extentReport.flush();
    }

    public synchronized static ExtentTest getTest() {
        return extentTest.get();
    }

    public synchronized static ExtentTest createTest(String name, String description) {
        ExtentTest test = extentReport.createTest(name, description);
        extentTest.set(test);
        return getTest();

    }

    public synchronized static void log(String message) {
        getTest().info(message);

    }

    public synchronized static void pass(String message) {
        getTest().pass(message);

    }

    public synchronized static void fail(String message) {
        getTest().fail(message);

    }


}
