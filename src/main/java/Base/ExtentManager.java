package Base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager extends BasePage {

    public static ExtentReports extentReport;
    public static String extentReportPrefix;
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public ExtentManager() throws IOException {
        super();
    }

    public static ExtentReports getReport() {
        if(extentReport == null) {
            setupExtentReport("Live Project 1");
        }
        return extentReport;
    }

    public static ExtentReports setupExtentReport(String testName) {
        extentReport = new ExtentReports();

        // Report klasörünü oluştur
        String reportPath = System.getProperty("user.dir") + "/target/reports/";
        File reportDir = new File(reportPath);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath +
                extentReportsPrefix_Name(testName) + ".html");
        extentReport.attachReporter(spark);

        extentReport.setSystemInfo("Tester", "My Name");
        spark.config().setReportName("Regression Test");
        spark.config().setDocumentTitle("Test Results");
        spark.config().setTheme(Theme.DARK);

        return extentReport;
    }

    public static String extentReportsPrefix_Name (String testName) {
        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        extentReportPrefix = testName + "_" + date;
        return extentReportPrefix;
    }

    public static void flushReport() {
        if (extentReport != null) {
            extentReport.flush();
        }
    }

    public synchronized static ExtentTest getTest() {
        return extentTest.get();
    }

    public synchronized static ExtentTest createTest(String name, String description) {
        if (extentReport == null) {
            getReport();
        }
        ExtentTest test = extentReport.createTest(name, description);
        extentTest.set(test);
        return getTest();
    }

    public synchronized static void log(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.info(message);
            System.out.println("LOG: " + message);
        } else {
            // Test yoksa oluştur
            System.out.println("Creating new test for: " + message);
            createTest("AutoCreatedTest", "Auto-created test");
            getTest().info(message);
        }
    }

    public synchronized static void pass(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.pass(message);
        } else {
            System.out.println("ExtentTest not initialized (PASS): " + message);
        }
    }

    public synchronized static void fail(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.fail(message);
        } else {
            System.out.println("ExtentTest not initialized (FAIL): " + message);
        }
    }

    public synchronized static void attachImage() {
        ExtentTest test = getTest();
        if (test != null && getScreenshotDestinationPath() != null) {
            test.addScreenCaptureFromPath(getScreenshotDestinationPath());
        }
    }
}