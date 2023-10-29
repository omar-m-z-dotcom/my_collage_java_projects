package demo;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest {

    String path = System.getProperty("user.dir");
    
    public String getCellData(int row, int col) throws Exception {
        XSSFSheet ExcelWSheet;
        XSSFWorkbook ExcelWBook;

        try {
            // Open the Excel file
            FileInputStream ExcelFile = new FileInputStream(path + "/Login.xlsx");

            // Access the required test data sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet("Sheet1");
            String cellData = ExcelWSheet.getRow(row).getCell(col).getStringCellValue();
            ExcelWBook.close();
            return cellData;
        }
        catch (IOException e){
            return "Error in opening the file";
        }
    }
    
    @Test
    public void FirefoxInvalidTest() throws Exception {

    	String username = getCellData(1, 0);
    	String password = getCellData(1, 1);

        // Set Firefox Web Driver
        System.setProperty("webdriver.gecko.driver", path + "/geckodriver.exe");
    	WebDriver driver = new FirefoxDriver();

        // Puts an Implicit wait, Will wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      
        // Launch website
        driver.navigate().to("https://www.vezeeta.com/en");
        // Maximize the browser
        driver.manage().window().maximize();
        
        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/div[2]/div[2]/div/a[2]")).click();

        // Enter the username and password
        driver.findElement(By.id("formik-input__input--email")).sendKeys(username);
        driver.findElement(By.id("formik-input__input--password")).sendKeys(password);

        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/div/div[2]/form/fieldset/button")).click();

        try {
            // Check whether a certain element appears which confirms that the login was not successful
            assertEquals(false, driver.findElement(By.id("selectSpecialityDropdown")).isDisplayed()); 
        } catch(NoSuchElementException e){
            assertTrue(true);
        }
        Thread.sleep(2000);
        
        // Close the web driver
        driver.close();
    }

    @Test
    public void FirefoxValidTest() throws Exception {

    	String username = getCellData(2, 0);
    	String password = getCellData(2, 1);

        // Set Firefox Web Driver
        System.setProperty("webdriver.gecko.driver", path + "/geckodriver.exe");
    	WebDriver driver = new FirefoxDriver();

        // Puts an Implicit wait, Will wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      
        // Launch website
        driver.navigate().to("https://www.vezeeta.com/en");
        // Maximize the browser
        driver.manage().window().maximize();
        
        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/div[2]/div[2]/div/a[2]")).click();

        // Enter the username and password
        driver.findElement(By.id("formik-input__input--email")).sendKeys(username);
        driver.findElement(By.id("formik-input__input--password")).sendKeys(password);

        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/div/div[2]/form/fieldset/button")).click();

        // Check whether a certain element appears which confirms that the login was successful
        assertEquals(true, driver.findElement(By.id("selectSpecialityDropdown")).isDisplayed());
        Thread.sleep(3000);
        driver.close();
    }

    @Test
    public void ChromeInvalidTest() throws Exception {

    	String username = getCellData(1, 0);
    	String password = getCellData(1, 1);

        // Set Google Chrome Web Driver
    	System.setProperty("webdriver.chrome.driver", path + "/chromedriver.exe");
    	WebDriver driver = new ChromeDriver();

        // Puts an Implicit wait, Will wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      
        // Launch website
        driver.navigate().to("https://www.vezeeta.com/en");
        // Maximize the browser
        driver.manage().window().maximize();
        
        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/div[2]/div[2]/div/a[2]")).click();

        // Enter the username and password
        driver.findElement(By.id("formik-input__input--email")).sendKeys(username);
        driver.findElement(By.id("formik-input__input--password")).sendKeys(password);

        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/div/div[2]/form/fieldset/button")).click();

        try {
            // Check whether a certain element appears which confirms that the login was not successful
            assertEquals(false, driver.findElement(By.id("selectSpecialityDropdown")).isDisplayed()); 
        } catch(NoSuchElementException e){
            assertTrue(true);
        }
        Thread.sleep(2000);
        driver.close();
    }

    @Test
    public void ChromeValidTest() throws Exception {

    	String username = getCellData(2, 0);
    	String password = getCellData(2, 1);

        // Set Google Chrome Web Driver
    	System.setProperty("webdriver.chrome.driver", path + "/chromedriver.exe");
    	WebDriver driver = new ChromeDriver();

        // Puts an Implicit wait, Will wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      
        // Launch website
        driver.navigate().to("https://www.vezeeta.com/en");
        // Maximize the browser
        driver.manage().window().maximize();
        
        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/div[2]/div[2]/div/a[2]")).click();

        // Enter the username and password
        driver.findElement(By.id("formik-input__input--email")).sendKeys(username);
        driver.findElement(By.id("formik-input__input--password")).sendKeys(password);

        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/div/div[2]/form/fieldset/button")).click();

        // Check whether a certain element appears which confirms that the login was successful
        assertEquals(true, driver.findElement(By.id("selectSpecialityDropdown")).isDisplayed());
        Thread.sleep(3000);
        driver.close();
    }

    @Test
    public void EdgeInvalidTest() throws Exception {

    	String username = getCellData(1, 0);
    	String password = getCellData(1, 1);

        // Set Edge Web Driver
    	System.setProperty("webdriver.edge.driver", path + "/msedgedriver.exe");
    	WebDriver driver = new EdgeDriver();

        // Puts an Implicit wait, Will wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      
        // Launch website
        driver.navigate().to("https://www.vezeeta.com/en");
        // Maximize the browser
        driver.manage().window().maximize();
        
        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/div[2]/div[2]/div/a[2]")).click();

        // Enter the username and password
        driver.findElement(By.id("formik-input__input--email")).sendKeys(username);
        driver.findElement(By.id("formik-input__input--password")).sendKeys(password);

        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/div/div[2]/form/fieldset/button")).click();

        try {
            // Check whether a certain element appears which confirms that the login was not successful
            assertEquals(false, driver.findElement(By.id("selectSpecialityDropdown")).isDisplayed()); 
        } catch(NoSuchElementException e){
            assertTrue(true);
        }
        Thread.sleep(2000);
        driver.close();
    }

    @Test
    public void EdgeValidTest() throws Exception {

    	String username = getCellData(2, 0);
    	String password = getCellData(2, 1);

        // Set Edge Web Driver
    	System.setProperty("webdriver.edge.driver", path + "/msedgedriver.exe");
    	WebDriver driver = new EdgeDriver();

        // Puts an Implicit wait, Will wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      
        // Launch website
        driver.navigate().to("https://www.vezeeta.com/en");
        // Maximize the browser
        driver.manage().window().maximize();
        
        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/div[2]/div[2]/div/a[2]")).click();

        // Enter the username and password
        driver.findElement(By.id("formik-input__input--email")).sendKeys(username);
        driver.findElement(By.id("formik-input__input--password")).sendKeys(password);

        // Click on Sign in button
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/div/div[2]/form/fieldset/button")).click();

        // Check whether a certain element appears which confirms that the login was successful
        assertEquals(true, driver.findElement(By.id("selectSpecialityDropdown")).isDisplayed());
        Thread.sleep(3000);
        driver.close();
    }
}
