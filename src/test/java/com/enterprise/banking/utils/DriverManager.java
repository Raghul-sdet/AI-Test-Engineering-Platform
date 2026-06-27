package com.enterprise.banking.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {

    /**
     * Factory method to initialize the WebDriver based on execution mode (Local or Grid).
     * @param browser The browser name (chrome, firefox, edge)
     * @param isHeadless Boolean to determine if headless mode should be used
     * @param executionMode The mode of execution (local or grid)
     * @param gridUrl The Selenium Grid Hub URL
     * @return WebDriver instance (Local or Remote)
     */
    public static WebDriver createDriver(String browser, boolean isHeadless, String executionMode, String gridUrl) {
        WebDriver driver = null;
        boolean isGrid = executionMode.trim().equalsIgnoreCase("grid");

        try {
            switch (browser.toLowerCase()) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isHeadless) firefoxOptions.addArguments("--headless");
                    
                    if (isGrid) {
                        driver = new RemoteWebDriver(new URL(gridUrl), firefoxOptions);
                    } else {
                        driver = new FirefoxDriver(firefoxOptions);
                    }
                    break;
                    
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (isHeadless) edgeOptions.addArguments("--headless");
                    
                    if (isGrid) {
                        driver = new RemoteWebDriver(new URL(gridUrl), edgeOptions);
                    } else {
                        driver = new EdgeDriver(edgeOptions);
                    }
                    break;
                    
                case "chrome":
                default:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (isHeadless) chromeOptions.addArguments("--headless=new");
                    
                    // Enterprise arguments to prevent memory crashes in Docker
                    chromeOptions.addArguments("--disable-dev-shm-usage"); 
                    chromeOptions.addArguments("--no-sandbox");
                    
                    if (isGrid) {
                        driver = new RemoteWebDriver(new URL(gridUrl), chromeOptions);
                    } else {
                        driver = new ChromeDriver(chromeOptions);
                    }
                    break;
            }
        } catch (MalformedURLException e) {
            // Fails the test immediately if the URL format in config.properties is invalid
            throw new RuntimeException("CRITICAL: Invalid Selenium Grid URL provided: " + gridUrl, e);
        }
        
        return driver;
    }
}