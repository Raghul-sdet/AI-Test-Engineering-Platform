package com.enterprise.banking.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enterprise.banking.config.SiteProfileLoader;
import com.enterprise.banking.utils.LocatorParser;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private SiteProfileLoader profileLoader;

    // Constructor
    public LoginPage(WebDriver driver, SiteProfileLoader profileLoader) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.profileLoader = profileLoader;
    }

    // Actions
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("login", "usernameField")))).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("login", "passwordField"))).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("login", "submitButton"))).click();
    }

    public void loginToBanking(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // Added for Hybrid E2E Test Compatibility
    public void login(String username, String password) {
        loginToBanking(username, password);
    }

    // Added for Hybrid E2E Test Compatibility
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("login", "logoutLink")))).click();
    }

    /**
     * Explicitly waits for the login to complete by verifying the presence of
     * the "Log Out" link, which only appears after a successful authentication.
     * Call this after clickLogin() before navigating to any other page.
     */
    public void waitForLoginSuccess() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("login", "logoutLink"))));
    }

    // Verify successful login
    public boolean isOverviewDisplayed() {
        try {
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("login", "accountOverviewHeader"))));
            return header.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Verify invalid login
    public String getErrorMessage() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("login", "errorMessage"))));
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }
}