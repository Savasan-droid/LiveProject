package Base;

import java.io.IOException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class Hooks extends BasePage {

    public Hooks() throws IOException {
        super();
    }

    @BeforeSuite
    public void suiteSetup() {
        System.out.println("=== Test Suite Starting ===");
        // ExtentReports'u başlat
        ExtentManager.getReport();
    }

    @BeforeTest
    public void setup() throws IOException {
        System.out.println("=== Individual Test Starting ===");
        getDriver().get(getUrl());
    }

    @AfterTest
    public void tearDown() {
        System.out.println("=== Individual Test Ending ===");
        WebDriverInstance.cleanupDriver();
    }

    @AfterSuite
    public void suiteTearDown() {
        System.out.println("=== Test Suite Ending ===");
        // Raporları kaydet
        ExtentManager.flushReport();
        System.out.println("Reports flushed successfully!");
    }
}