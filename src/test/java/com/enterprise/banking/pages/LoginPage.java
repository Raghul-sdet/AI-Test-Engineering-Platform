package com.enterprise.banking.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.xpath("//input[@value='Log In']");
    private By accountOverviewHeader = By.xpath("//h1[@class='title']");
    private By errorMessage = By.className("error");
    private By logoutLink = By.linkText("Log Out");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Actions
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
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
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    // Verify successful login
    public boolean isOverviewDisplayed() {
        try {
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(accountOverviewHeader));
            return header.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Verify invalid login
    public String getErrorMessage() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }
}