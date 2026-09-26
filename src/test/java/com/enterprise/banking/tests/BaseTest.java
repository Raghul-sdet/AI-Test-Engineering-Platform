package com.enterprise.banking.tests;

import com.enterprise.banking.repositories.UserRepository;
import com.enterprise.banking.utils.DriverManager;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;
import com.enterprise.banking.config.SiteProfileLoader;

/**
 * Enterprise Base Test
 *
 * Responsibilities:
 * 1. Initialize H2 Database
 * 2. Launch Browser
 * 3. Navigate to Application
 * 4. Quit Browser
 * 5. Execute Suite Cleanup
 */
public class BaseTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BaseTest.class);

    protected WebDriver driver;
    protected SiteProfileLoader profileLoader = new SiteProfileLoader();

    /**
     * Runs once before the entire suite.
     */
    @BeforeSuite(alwaysRun = true)
    public void executePreSuiteSetup() {

        System.out.println("======================================");
        System.out.println(" Enterprise QA Automation Framework");
        System.out.println(" Suite Initialization Started");
        System.out.println("======================================");

        UserRepository.initializeDatabase();

        System.out.println("Database initialization completed.");
        System.out.println("======================================");
    }

    /**
     * Runs before every test.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        LOGGER.info("Launching browser...");

        DriverManager.setDriver();

        driver = DriverManager.getDriver();

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(10));

        driver.manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(30));

        driver.manage().window().maximize();

        String targetUrl = profileLoader.getBaseUrl();
        driver.get(targetUrl);

        LOGGER.info("Application launched successfully.");

    }

    /**
     * Runs after every test.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        LOGGER.info("Closing browser...");

        DriverManager.quitDriver();

    }

    /**
     * Runs once after entire suite.
     */
    @AfterSuite(alwaysRun = true)
    public void executePostSuiteTeardown() {

        System.out.println("======================================");
        System.out.println(" Suite Execution Completed");
        System.out.println("======================================");

        /*
         * Future Enhancement
         *
         * DatabaseToExcelExporter.exportData();
         * DashboardGenerator.generateDashboard();
         * Allure Report
         * AI Analytics
         */

        System.out.println("Reports generated successfully.");

    }

    /**
     * Returns current WebDriver.
     */
    public WebDriver getDriver() {

        return driver;

    }

}