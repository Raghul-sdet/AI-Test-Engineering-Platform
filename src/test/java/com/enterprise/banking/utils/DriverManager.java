package com.enterprise.banking.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the WebDriver lifecycle using ThreadLocal for thread-safe parallel execution.
 * Updated to adhere to Selenium 4 standard practices, eliminating deprecated DesiredCapabilities.
 */
public final class DriverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private DriverManager() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Retrieves the active WebDriver instance for the current execution thread.
     *
     * @return Thread-safe WebDriver instance.
     */
    public static WebDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    /**
     * Initializes the WebDriver with modern ChromeOptions.
     * Leverages Selenium 4's built-in Selenium Manager for binary resolution.
     */
    public static void setDriver() {
        if (DRIVER_THREAD_LOCAL.get() == null) {
            ChromeOptions options = new ChromeOptions();

            // Check config.properties for headless=true (CI/CD environments like GitHub Actions
            // have no display server, so headless is mandatory there).
            String headlessProp = ConfigReader.getProperty("headless");
            boolean headless = "true".equalsIgnoreCase(headlessProp);

            if (headless) {
                options.addArguments("--headless=new");         // Modern headless mode (Chrome 112+)
                options.addArguments("--no-sandbox");           // Required in containerised/CI environments
                options.addArguments("--disable-gpu");          // Avoids GPU-related crashes in headless mode
                options.addArguments("--disable-dev-shm-usage"); // Prevents crashes on runners with small /dev/shm
                options.addArguments("--window-size=1920,1080"); // Fixed size replaces --start-maximized in headless
                LOGGER.info("WebDriver running in HEADLESS mode (CI/CD environment detected).");
            } else {
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--start-maximized");
                LOGGER.info("WebDriver running in NORMAL (headed) mode.");
            }

            WebDriver driver = new ChromeDriver(options);
            DRIVER_THREAD_LOCAL.set(driver);
            LOGGER.debug("WebDriver initialized successfully for thread ID: {}", Thread.currentThread().getId());
        }
    }

    /**
     * Terminates the WebDriver and forcefully clears the ThreadLocal variable
     * to prevent JVM memory leaks during large suite executions.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception exception) {
                LOGGER.warn("Exception encountered during driver teardown: {}", exception.getMessage());
            } finally {
                DRIVER_THREAD_LOCAL.remove();
                LOGGER.debug("ThreadLocal driver reference removed for thread ID: {}", Thread.currentThread().getId());
            }
        }
    }
}