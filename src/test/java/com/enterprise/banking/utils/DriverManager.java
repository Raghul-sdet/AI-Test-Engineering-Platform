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
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--start-maximized");
            // Headless mode can be injected here via environment variables if required for CI/CD

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