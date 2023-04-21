package resources;

import base.BasePageAPI;
import base.ExtentReportManagerAPI;
import io.cucumber.java.Scenario;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class ListenersAPI extends BasePageAPI implements ITestListener {

    public ListenersAPI() throws IOException {
        super();
    }

    public synchronized void onTestStart(ITestResult result) {
        System.out.println("onTestStart");
        Scenario scenario;
        System.out.println(result.getTestContext().getSuite().getName());
        ExtentReportManagerAPI.getReport();
        ExtentReportManagerAPI.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
    }

    public synchronized void onTestFailure(ITestResult result) {
        ExtentReportManagerAPI.getTest().fail(result.getThrowable());

    }

    public synchronized void onFinish(ITestContext context) {
        ExtentReportManagerAPI.flushReport();
    }

}
