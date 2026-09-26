package com.enterprise.banking.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import com.enterprise.banking.config.SiteProfileLoader;
import com.enterprise.banking.utils.LocatorParser;

public class TransferFundsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private SiteProfileLoader profileLoader;

    // Constructor
    public TransferFundsPage(WebDriver driver, SiteProfileLoader profileLoader) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.profileLoader = profileLoader;
    }

    // --- Methods ---
    public void navigateToTransferFunds() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "linkTransferFunds")))).click();
    }

    public void enterTransferAmount(String amount) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "amountField")))).sendKeys(amount);
    }

    public void selectFromAccountByIndex(int index) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "fromAccountDropdown"))));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "fromAccountOptions")), 0));
                
        Select fromAccount = new Select(driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "fromAccountDropdown"))));
        fromAccount.selectByIndex(index);
    }

    public void selectToAccountByIndex(int index) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "toAccountDropdown"))));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "toAccountOptions")), 0));
                
        Select toAccount = new Select(driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "toAccountDropdown"))));
        toAccount.selectByIndex(index);
    }

    public void clickTransfer() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "submitButton")))).click();
    }

    public boolean isTransferComplete() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "textSuccessHeader")))).getText().equals("Transfer Complete!");
        } catch (Exception e) {
            return false;
        }
    }

    public String getTransferredAmountText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "textAmountResult")))).getText();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "btnLogout")))).click();
    }

    // --- Added for Hybrid E2E Test ---
    
    public void executeTransfer(String amount, String fromAcc, String toAcc) {
        enterTransferAmount(amount);

        // Wait for dropdowns to populate from backend
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "fromAccountOptions")), 0));
        Select fromAccount = new Select(driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "fromAccountDropdown"))));
        fromAccount.selectByVisibleText(fromAcc);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "toAccountOptions")), 0));
        Select toAccount = new Select(driver.findElement(LocatorParser.parseLocator(profileLoader.getLocator("transferFunds", "toAccountDropdown"))));
        toAccount.selectByVisibleText(toAcc);

        clickTransfer();
    }
}