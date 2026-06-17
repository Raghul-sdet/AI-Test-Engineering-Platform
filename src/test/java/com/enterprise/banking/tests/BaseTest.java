package com.enterprise.banking.tests;

import com.enterprise.banking.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    // ThreadLocal ensures thread safety for parallel execution in CI/CD
    protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    
    // CRITICAL FIX: Re-adding the protected driver variable so existing tests compile perfectly
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.getProperty("browser").toLowerCase();
        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));

        WebDriver localDriver;

        switch (browser) {
            case "firefox":
                localDriver = new FirefoxDriver();
                break;
            case "edge":
                localDriver = new EdgeDriver();
                break;
            case "chrome":
            default:
                ChromeOptions options = new ChromeOptions();
                if (isHeadless) options.addArguments("--headless=new");
                localDriver = new ChromeDriver(options);
                break;
        }

        int implicitWait = Integer.parseInt(ConfigReader.getProperty("implicitWait"));
        localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        localDriver.manage().window().maximize();
        localDriver.get(ConfigReader.getProperty("baseUrl") + "index.htm");

        // Set the ThreadLocal driver
        threadLocalDriver.set(localDriver);
        
        // Map it back to the standard driver variable for backward compatibility
        driver = getDriver();
    }

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            threadLocalDriver.remove();
        }
    }
}