package com.enterprise.banking.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import com.enterprise.banking.config.SiteProfileLoader;
import com.enterprise.banking.utils.LocatorParser;

public class FindTransactionsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private SiteProfileLoader profileLoader;

    // --- Constructor ---
    public FindTransactionsPage(WebDriver driver, SiteProfileLoader profileLoader) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.profileLoader = profileLoader;
    }

    // --- Methods ---
    public void navigateToFindTransactions() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "linkFindTransactions")))).click();
        // Wait for the form to be ready for input
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "containerForm"))));
    }

    public void searchByAmount(String amount) {
        WebElement inputAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "inputAmount"))));
        inputAmount.clear();
        inputAmount.sendKeys(amount);
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "btnFindByAmount")))).click();
        waitForAjaxResults();
    }

    public void searchByDate(String date) {
        WebElement inputDate = wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "inputDate"))));
        inputDate.clear();
        inputDate.sendKeys(date);
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "btnFindByDate")))).click();
        waitForAjaxResults();
    }
    
    public void searchByTransactionId(String transactionId) {
        WebElement inputTransactionId = wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "inputTransactionId"))));
        inputTransactionId.clear();
        inputTransactionId.sendKeys(transactionId);
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "btnFindById")))).click();
        waitForAjaxResults();
    }

    /**
     * Synchronizes the WebDriver with the Parabank AJAX call.
     * It waits for the form to hide and the results container to appear.
     */
    private void waitForAjaxResults() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "containerForm"))));
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "containerResult"))));
    }

    public boolean areTransactionsFound() {
        try {
            // Wait for at least one row to populate in the table body
            By rowsLocator = LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "rowsTransactionResults"));
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(rowsLocator, 0));
            return driver.findElements(rowsLocator).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int getTransactionCount() {
        By rowsLocator = LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "rowsTransactionResults"));
        return driver.findElements(rowsLocator).size();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("findTransactions", "btnLogout")))).click();
    }
}