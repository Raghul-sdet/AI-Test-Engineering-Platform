package com.enterprise.banking.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TransferFundsPage {

    private WebDriverWait wait;

    // --- Locators ---
    @FindBy(linkText = "Transfer Funds")
    private WebElement linkTransferFunds;

    @FindBy(id = "amount")
    private WebElement inputAmount;

    @FindBy(id = "fromAccountId")
    private WebElement dropdownFromAccountId;

    @FindBy(id = "toAccountId")
    private WebElement dropdownToAccountId;

    @FindBy(xpath = "//input[@value='Transfer']")
    private WebElement btnTransferSubmit;

    @FindBy(xpath = "//div[@id='showResult']//h1[@class='title']")
    private WebElement textSuccessHeader;

    @FindBy(id = "amountResult")
    private WebElement textAmountResult;

    @FindBy(linkText = "Log Out")
    private WebElement btnLogout;

    // Constructor
    public TransferFundsPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // --- Methods ---
    public void navigateToTransferFunds() {
        wait.until(ExpectedConditions.elementToBeClickable(linkTransferFunds)).click();
    }

    public void enterTransferAmount(String amount) {
        wait.until(ExpectedConditions.visibilityOf(inputAmount)).sendKeys(amount);
    }

    public void selectFromAccountByIndex(int index) {
        wait.until(ExpectedConditions.visibilityOf(dropdownFromAccountId));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath("//select[@id='fromAccountId']/option"), 0));
                
        Select fromAccount = new Select(dropdownFromAccountId);
        fromAccount.selectByIndex(index);
    }

    public void selectToAccountByIndex(int index) {
        wait.until(ExpectedConditions.visibilityOf(dropdownToAccountId));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath("//select[@id='toAccountId']/option"), 0));
                
        Select toAccount = new Select(dropdownToAccountId);
        toAccount.selectByIndex(index);
    }

    public void clickTransfer() {
        wait.until(ExpectedConditions.elementToBeClickable(btnTransferSubmit)).click();
    }

    public boolean isTransferComplete() {
        try {
            wait.until(ExpectedConditions.visibilityOf(textSuccessHeader));
            return textSuccessHeader.getText().equals("Transfer Complete!");
        } catch (Exception e) {
            return false;
        }
    }

    public String getTransferredAmountText() {
        wait.until(ExpectedConditions.visibilityOf(textAmountResult));
        return textAmountResult.getText();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLogout)).click();
    }

    // --- Added for Hybrid E2E Test ---
    
    public void executeTransfer(String amount, String fromAcc, String toAcc) {
        enterTransferAmount(amount);

        // Wait for dropdowns to populate from backend
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath("//select[@id='fromAccountId']/option"), 0));
        Select fromAccount = new Select(dropdownFromAccountId);
        fromAccount.selectByVisibleText(fromAcc);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath("//select[@id='toAccountId']/option"), 0));
        Select toAccount = new Select(dropdownToAccountId);
        toAccount.selectByVisibleText(toAcc);

        clickTransfer();
    }
}