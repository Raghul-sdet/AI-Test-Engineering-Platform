package com.enterprise.banking.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {
    
    // Capturing as Base64 prevents broken image links in Jenkins HTML reports
    public static String getBase64Screenshot(WebDriver driver) {
        if (driver != null) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        }
        return "";
    }
}