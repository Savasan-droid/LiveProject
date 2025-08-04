package Listeners;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import Base.BasePage;
import Base.ExtentManager;

public class Listeners extends BasePage implements ITestListener {

    public Listeners() throws IOException {
        super();
    }

    public synchronized void onStart(ITestContext context) {
        ExtentManager.getReport();
        ExtentManager.createTest(context.getName(), context.getName());
    }

    public synchronized void onTestStart(ITestResult result) {
        // Her test başladığında yeni bir test oluştur
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        if (testDescription == null || testDescription.isEmpty()) {
            testDescription = testName;
        }
        ExtentManager.createTest(testName, testDescription);
    }

    public synchronized void onTestFailure(ITestResult result) {
        ExtentManager.getTest().fail(result.getThrowable());
        try {
            System.out.println("Test failed: " + result.getName());
            takeSnapShot(result.getMethod().getMethodName());
            ExtentManager.attachImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void onFinish(ITestContext context) {
        ExtentManager.flushReport();
    }
}