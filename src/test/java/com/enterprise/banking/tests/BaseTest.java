package com.enterprise.banking.tests;

import com.enterprise.banking.repositories.UserRepository;
import com.enterprise.banking.utils.ConfigReader;
import com.enterprise.banking.utils.DriverManager;
import com.enterprise.banking.utils.ReportGenerator;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    // This is the variable all your test classes (LoginPage, etc.) need
    protected WebDriver driver;
    
    // ThreadLocal for parallel execution safety
    private static ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverLocal.get();
    }

    // PHASE 1: Initialize H2 Database Schema before any tests run
    @BeforeSuite(alwaysRun = true)
    public void setupGlobalFramework() {
        System.out.println(">>> Executing Pre-Suite Setup...");
        UserRepository.initializeSchema();
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional String browser) {
        if (browser == null) {
            browser = ConfigReader.getProperty("browser");
        }
        
        // Read execution configurations
        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
        String executionMode = ConfigReader.getProperty("execution.mode");
        String gridUrl = ConfigReader.getProperty("gridUrl");

        // Initialize driver using the factory with Grid support
        driver = DriverManager.createDriver(browser, isHeadless, executionMode, gridUrl);
        
        // Store in ThreadLocal
        driverLocal.set(driver);
        
        driver.manage().window().maximize();
        
        String url = ConfigReader.getProperty("baseUrl");
        driver.get(url);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driverLocal.remove();
        }
    }

    // PHASE 4: Generate Excel Report after all parallel threads have finished
    @AfterSuite(alwaysRun = true)
    public void generateFinalReport() {
        System.out.println(">>> Executing Post-Suite Teardown...");
        ReportGenerator.generateExecutionReport();
    }
}