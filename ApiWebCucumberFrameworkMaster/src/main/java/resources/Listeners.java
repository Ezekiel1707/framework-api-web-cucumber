package resources;

import java.io.IOException;
import base.BasePageUI;
import base.ExtentReportManager;
import io.cucumber.java.Scenario;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listeners extends BasePageUI implements ITestListener {

    public Listeners() throws IOException {
        super();
    }

    public synchronized void onTestStart(ITestResult result) {
        System.out.println("onTestStart");
        Scenario scenario;
        System.out.println(result.getTestContext().getSuite().getName());
        ExtentReportManager.getReport();
        ExtentReportManager.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
    }

    public synchronized void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().fail(result.getThrowable());
        try {
            System.out.println("Test failed: " + result.getName());
            takeSnapShot(result.getMethod().getMethodName());
            ExtentReportManager.attachImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void onFinish(ITestContext context) {
        ExtentReportManager.flushReport();
    }

}
