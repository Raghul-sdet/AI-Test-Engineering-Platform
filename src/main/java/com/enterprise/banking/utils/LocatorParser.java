package com.enterprise.banking.utils;

import org.openqa.selenium.By;

public class LocatorParser {
    
    public static By parseLocator(String locatorString) {
        if (locatorString == null || !locatorString.contains("=")) {
            throw new IllegalArgumentException("Invalid locator format. Expected prefix=value, got: " + locatorString);
        }
        
        String[] parts = locatorString.split("=", 2);
        String prefix = parts[0].trim();
        String value = parts[1].trim();
        
        switch (prefix.toLowerCase()) {
            case "id":
                return By.id(value);
            case "name":
                return By.name(value);
            case "xpath":
                return By.xpath(value);
            case "css":
                return By.cssSelector(value);
            case "classname":
                return By.className(value);
            case "linktext":
                return By.linkText(value);
            case "partiallinktext":
                return By.partialLinkText(value);
            case "tagname":
                return By.tagName(value);
            default:
                throw new IllegalArgumentException("Unsupported locator prefix: " + prefix);
        }
    }
}
