package uk.co.automationtesting;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.testng.annotations.Test;

import Base.ExtentManager;
import Base.Hooks;
import pageObjects.Homepage;
import pageObjects.ShopHomepage;
import pageObjects.ShopLoginPage;
import pageObjects.ShopYourAccount;

@org.testng.annotations.Listeners(Listeners.Listeners.class)
public class ShopLoginTest extends Hooks {

    public ShopLoginTest() throws IOException {
        super();
    }

    @Test
    public void shopLoginTest() throws IOException, InterruptedException {

        // Test başlatma - eğer listeners çalışmazsa manuel olarak
        try {
            ExtentManager.log("Starting ShopLoginTest...");
        } catch (Exception e) {
            // ExtentManager henüz başlatılmamışsa, manuel olarak başlat
            ExtentManager.getReport();
            ExtentManager.createTest("shopLoginTest", "Shop Login Test with Excel Data");
            ExtentManager.log("Starting ShopLoginTest...");
        }

        // creating an object of the automationtesting.co.uk webpage
        Homepage home = new Homepage();
        //handles cookie prompt
        home.getCookie().click();
        home.getTestStoreLink().click();

        /*
        If the code on line 38 doesn't handle the cookie prompt, comment out line 38 AND line 39 and uncomment lines 43, 44, 45:
        JavascriptExecutor jse = (JavascriptExecutor)getDriver();
        jse.executeScript("arguments[0].scrollIntoView()", home.getTestStoreLink());
        home.getTestStoreLink().click();
        */

        // creating an object of the test store homepage
        ShopHomepage shopHome = new ShopHomepage();
        shopHome.getLoginBtn().click();
        ExtentManager.pass("Successfully clicked login button");

        // Excel dosyasının yolu düzeltildi
        FileInputStream workbookLocation = new FileInputStream(System.getProperty("user.dir") +
                "/src/main/resources/credentials.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(workbookLocation);
        XSSFSheet sheet = workbook.getSheetAt(0);

        /****************************************************************************
         * Excel Spreadsheet Layout Reminder (teaching purposes only)
         *
         * |Row=0 -->| Email Address (Cell 0) Password (Cell 1) *
         * --------------------------------------------------------------------
         * |Row=1 -->| test@test.com (Cell 0) test123 (Cell 1)
         * |Row=2 -->| john.smith@test.com (Cell 0) test123 (Cell 1)
         * |Row=3 -->| lucy.jones@test.com (Cell 0) catlover1 (Cell 1)
         * |Row=4 -->| martin.brian@test.com (Cell 0) ilovepasta5 (Cell 1)
         ****************************************************************************/

        // First user test
        Row row1 = sheet.getRow(1);
        Cell cellR1C0 = row1.getCell(0);
        Cell cellR1C1 = row1.getCell(1);

        String emailRow1 = cellR1C0.toString();
        String passwordRow1 = cellR1C1.toString();

        System.out.println("Testing user 1: " + emailRow1);
        ExtentManager.log("Testing first user: " + emailRow1);

        ShopLoginPage shop = new ShopLoginPage();
        shop.getEmail().clear();
        shop.getEmail().sendKeys(emailRow1);
        shop.getPassword().clear();
        shop.getPassword().sendKeys(passwordRow1);

        Thread.sleep(2000);
        shop.getSubmitBtn().click();

        ShopYourAccount yourAcc = new ShopYourAccount();

        try {
            yourAcc.getSignOutBtn().click();
            ExtentManager.pass("User 1 HAS signed in successfully");
            Thread.sleep(1000);
        } catch (Exception e) {
            ExtentManager.fail("User 1 could NOT sign in");
            Assert.fail("User 1 login failed");
        }

        // Navigate back to login page for second user
        shopHome.getLoginBtn().click();
        Thread.sleep(1000);

        // Second user test
        Row row2 = sheet.getRow(2);
        Cell cellR2C0 = row2.getCell(0);
        Cell cellR2C1 = row2.getCell(1);

        String emailRow2 = cellR2C0.toString();
        String passwordRow2 = cellR2C1.toString();

        System.out.println("Testing user 2: " + emailRow2);
        ExtentManager.log("Testing second user: " + emailRow2);

        shop.getEmail().clear();
        shop.getEmail().sendKeys(emailRow2);
        shop.getPassword().clear();
        shop.getPassword().sendKeys(passwordRow2);
        Thread.sleep(2000);
        shop.getSubmitBtn().click();

        try {
            yourAcc.getSignOutBtn().click();
            ExtentManager.pass("User 2 HAS signed in successfully");
        } catch (Exception e) {
            ExtentManager.fail("User 2 could NOT sign in");
            Assert.fail("User 2 login failed");
        }

        // Close workbook
        workbook.close();
        workbookLocation.close();
    }
}
