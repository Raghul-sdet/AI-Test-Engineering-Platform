package com.enterprise.banking.tests;

import com.enterprise.banking.utils.DriverManager;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Foundational test configuration class.
 * Manages the setup and teardown routines utilizing the thread-safe DriverManager.
 */
public class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;

    /**
     * Configures the test environment prior to execution.
     * Implements modern Selenium 4 implicit wait configurations.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LOGGER.info("Initiating Pre-Test Configuration...");
        DriverManager.setDriver();
        driver = DriverManager.getDriver();
        
        // Modern Selenium 4 wait implementation replacing deprecated TimeUnit
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    /**
     * Cleans up the execution environment post-test to prevent resource exhaustion.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        LOGGER.info("Executing Post-Test Teardown...");
        DriverManager.quitDriver();
    }

    /**
     * Retrieves the active driver for the current test context.
     *
     * @return The WebDriver instance.
     */
    public WebDriver getDriver() {
        return driver;
    }
}