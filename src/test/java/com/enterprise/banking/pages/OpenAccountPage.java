package com.enterprise.banking.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import com.enterprise.banking.config.SiteProfileLoader;
import com.enterprise.banking.utils.LocatorParser;

public class OpenAccountPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private SiteProfileLoader profileLoader;

    // Constructor
    public OpenAccountPage(WebDriver driver, SiteProfileLoader profileLoader) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.profileLoader = profileLoader;
    }

    // --- Methods ---
    public void navigateToOpenNewAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "linkOpenNewAccount")))).click();
    }

    public void selectAccountType(String accountType) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "dropdownAccountType"))));
        Select typeSelect = new Select(driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "dropdownAccountType"))));
        typeSelect.selectByVisibleText(accountType);
    }

    public void selectFromAccountByIndex(int index) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "dropdownFromAccountId"))));
        
        // Explicit Wait: Force script to pause until the backend loads at least one account option
        wait.until(ExpectedConditions.presenceOfElementLocated(
                LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "fromAccountOption1"))));
                
        Select fromAccountSelect = new Select(driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "dropdownFromAccountId"))));
        fromAccountSelect.selectByIndex(index);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "btnSubmitOpenAccount")))).click();
    }

    public boolean isAccountOpenedSuccessfully() {
        try {
            // BULLETPROOF SYNC: If the unique account ID physically appears in the DOM,
            // it is the absolute proof that the account was created successfully.
            // We bypass the flaky header text extraction entirely.
            wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "textNewAccountId"))));
            return true; 
        } catch (Exception e) {
            return false;
        }
    }

    public String getGeneratedAccountNumber() {
        return driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "textNewAccountId"))).getText();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("openAccount", "btnLogout")))).click();
    }
}