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

    @BeforeMethod
    public void setUp() {
        // Setup Chrome Options for Jenkins/Headless environment
        ChromeOptions options = new ChromeOptions();
        
        // This makes the browser invisible so it can run on a server
        options.addArguments("--headless=new"); 
        // Essential for Linux/Server environments like Jenkins
        options.addArguments("--no-sandbox"); 
        // Prevents memory issues in containerized environments
        options.addArguments("--disable-dev-shm-usage"); 
        options.addArguments("--window-size=1920,1080");

        // Initialize the driver
        driver = new ChromeDriver(options);
        
        // Load configuration and set timeouts
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