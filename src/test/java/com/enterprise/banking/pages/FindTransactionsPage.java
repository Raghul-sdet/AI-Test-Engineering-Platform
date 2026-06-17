package com.enterprise.banking.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FindTransactionsPage {

    private WebDriverWait wait;

    // --- Locators ---
    @FindBy(linkText = "Find Transactions")
    private WebElement linkFindTransactions;

    @FindBy(id = "formContainer")
    private WebElement containerForm;

    @FindBy(id = "amount")
    private WebElement inputAmount;

    @FindBy(id = "findByAmount")
    private WebElement btnFindByAmount;

    @FindBy(id = "transactionDate")
    private WebElement inputDate;

    @FindBy(id = "findByDate")
    private WebElement btnFindByDate;

    @FindBy(id = "transactionId")
    private WebElement inputTransactionId;

    @FindBy(id = "findById")
    private WebElement btnFindById;

    @FindBy(id = "resultContainer")
    private WebElement containerResult;

    @FindBy(xpath = "//tbody[@id='transactionBody']/tr")
    private List<WebElement> rowsTransactionResults;

    @FindBy(linkText = "Log Out")
    private WebElement btnLogout;

    // --- Constructor ---
    public FindTransactionsPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // --- Methods ---
    public void navigateToFindTransactions() {
        wait.until(ExpectedConditions.elementToBeClickable(linkFindTransactions)).click();
        // Wait for the form to be ready for input
        wait.until(ExpectedConditions.visibilityOf(containerForm));
    }

    public void searchByAmount(String amount) {
        wait.until(ExpectedConditions.visibilityOf(inputAmount)).clear();
        inputAmount.sendKeys(amount);
        wait.until(ExpectedConditions.elementToBeClickable(btnFindByAmount)).click();
        waitForAjaxResults();
    }

    public void searchByDate(String date) {
        wait.until(ExpectedConditions.visibilityOf(inputDate)).clear();
        inputDate.sendKeys(date);
        wait.until(ExpectedConditions.elementToBeClickable(btnFindByDate)).click();
        waitForAjaxResults();
    }
    
    public void searchByTransactionId(String transactionId) {
        wait.until(ExpectedConditions.visibilityOf(inputTransactionId)).clear();
        inputTransactionId.sendKeys(transactionId);
        wait.until(ExpectedConditions.elementToBeClickable(btnFindById)).click();
        waitForAjaxResults();
    }

    /**
     * Synchronizes the WebDriver with the Parabank AJAX call.
     * It waits for the form to hide and the results container to appear.
     */
    private void waitForAjaxResults() {
        wait.until(ExpectedConditions.invisibilityOf(containerForm));
        wait.until(ExpectedConditions.visibilityOf(containerResult));
    }

    public boolean areTransactionsFound() {
        try {
            // Wait for at least one row to populate in the table body
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//tbody[@id='transactionBody']/tr"), 0));
            return rowsTransactionResults.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int getTransactionCount() {
        return rowsTransactionResults.size();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLogout)).click();
    }
}