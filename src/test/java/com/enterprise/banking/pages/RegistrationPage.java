package com.enterprise.banking.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import com.enterprise.banking.config.SiteProfileLoader;
import com.enterprise.banking.utils.LocatorParser;

public class RegistrationPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private SiteProfileLoader profileLoader;

    // Constructor
    public RegistrationPage(WebDriver driver, SiteProfileLoader profileLoader) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.profileLoader = profileLoader;
    }

    // --- Methods ---
    public void navigateToRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("registration", "linkRegister")))).click();
    }

    public void fillRegistrationForm(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputFirstName")))).sendKeys("QA");
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputLastName"))).sendKeys("Automation");
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputStreet"))).sendKeys("123 Testing Blvd");
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputCity"))).sendKeys("Tech City");
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputState"))).sendKeys("CA");
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputZipCode"))).sendKeys("90210");
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputPhoneNumber"))).sendKeys("555-0199");
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputSsn"))).sendKeys("000-11-2222");
        
        // Injecting the dynamic credentials
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputUsername"))).sendKeys(username);
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputPassword"))).sendKeys(password);
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("registration", "inputConfirmPassword"))).sendKeys(password);
    }

    public void clickSubmitRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("registration", "btnRegisterSubmit")))).click();
    }

    public String getRegistrationSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("registration", "textSuccessHeader")))).getText();
    }
}