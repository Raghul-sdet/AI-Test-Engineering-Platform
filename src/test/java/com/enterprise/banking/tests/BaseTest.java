package com.enterprise.banking.tests;

import com.enterprise.banking.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;

public class BaseTest {
    public static WebDriver driver;

    // --- THIS IS THE MISSING METHOD ---
    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); 
        options.addArguments("--no-sandbox"); 
        options.addArguments("--disable-dev-shm-usage"); 
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicitWait"))));
        driver.manage().window().maximize();
        driver.get(ConfigReader.getProperty("baseUrl"));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}