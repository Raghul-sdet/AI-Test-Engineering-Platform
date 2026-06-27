package com.enterprise.banking.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.util.stream.Collectors;

public class AllureUtils {

    /**
     * Captures a screenshot and attaches it to the Allure report.
     * @param driver The active ThreadLocal WebDriver instance
     * @return byte array of the screenshot
     */
    @Attachment(value = "Page Screenshot on Failure", type = "image/png")
    public static byte[] saveScreenshot(WebDriver driver) {
        if (driver != null) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    /**
     * Captures the HTML DOM page source and attaches it to the Allure report.
     * @param driver The active ThreadLocal WebDriver instance
     * @return String representation of the page source
     */
    @Attachment(value = "HTML Page Source", type = "text/html")
    public static String savePageSource(WebDriver driver) {
        if (driver != null) {
            return driver.getPageSource();
        }
        return "Page source not available.";
    }

    /**
     * Captures browser console logs to identify JavaScript or network errors.
     * @param driver The active ThreadLocal WebDriver instance
     * @return String containing all browser logs
     */
    @Attachment(value = "Browser Console Logs", type = "text/plain")
    public static String saveBrowserLogs(WebDriver driver) {
        if (driver != null) {
            try {
                LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
                return logEntries.getAll().stream()
                        .map(LogEntry::toString)
                        .collect(Collectors.joining(System.lineSeparator()));
            } catch (Exception e) {
                return "Browser console logs not supported or available for this driver instance.";
            }
        }
        return "Driver instance is null.";
    }

    /**
     * Attaches a simple text message to the report.
     * @param message The text message to attach
     * @return The same message string
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    /**
     * Dynamically generates the environment.properties file in the allure-results folder
     * to display execution environment details on the Allure Dashboard.
     */
    public static void generateEnvironmentDetails() {
        try {
            java.util.Properties props = new java.util.Properties();
            
            // Adding environment details
            props.setProperty("Framework", "Enterprise Hybrid QA Architecture");
            props.setProperty("Environment", "QA");
            
            // Fetching from your config.properties
            String executionMode = ConfigReader.getProperty("execution.mode");
            if(executionMode != null) props.setProperty("Execution Mode", executionMode.toUpperCase());
            
            String browser = ConfigReader.getProperty("browser");
            if(browser != null) props.setProperty("Target Browser", browser.toUpperCase());
            
            // System properties
            props.setProperty("Operating System", System.getProperty("os.name"));
            props.setProperty("Java Version", System.getProperty("java.version"));
            
            // Ensure the allure-results directory exists
            java.io.File resultsFolder = new java.io.File("target/allure-results");
            if (!resultsFolder.exists()) {
                resultsFolder.mkdirs();
            }
            
            // Write to environment.properties
            java.io.FileOutputStream fos = new java.io.FileOutputStream(new java.io.File(resultsFolder, "environment.properties"));
            props.store(fos, "Allure Report Environment Variables");
            fos.close();
            
            System.out.println(">>> Allure environment variables generated successfully.");
        } catch (Exception e) {
            System.err.println(">>> Failed to generate Allure environment properties: " + e.getMessage());
        }
    }
}