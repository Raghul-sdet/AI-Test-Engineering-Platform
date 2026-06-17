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

public class OpenAccountPage {

    private WebDriverWait wait;

    // --- Locators ---
    @FindBy(linkText = "Open New Account")
    private WebElement linkOpenNewAccount;

    @FindBy(id = "type")
    private WebElement dropdownAccountType;

    @FindBy(id = "fromAccountId")
    private WebElement dropdownFromAccountId;

    @FindBy(xpath = "//input[@value='Open New Account' or @type='submit']")
    private WebElement btnSubmitOpenAccount;

    // The ultimate anchor for our AJAX synchronization
    @FindBy(id = "newAccountId")
    private WebElement textNewAccountId;

    @FindBy(linkText = "Log Out")
    private WebElement btnLogout;

    // Constructor
    public OpenAccountPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // --- Methods ---
    public void navigateToOpenNewAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(linkOpenNewAccount)).click();
    }

    public void selectAccountType(String accountType) {
        wait.until(ExpectedConditions.visibilityOf(dropdownAccountType));
        Select typeSelect = new Select(dropdownAccountType);
        typeSelect.selectByVisibleText(accountType);
    }

    public void selectFromAccountByIndex(int index) {
        wait.until(ExpectedConditions.visibilityOf(dropdownFromAccountId));
        
        // Explicit Wait: Force script to pause until the backend loads at least one account option
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[@id='fromAccountId']/option[1]")));
                
        Select fromAccountSelect = new Select(dropdownFromAccountId);
        fromAccountSelect.selectByIndex(index);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSubmitOpenAccount)).click();
    }

    public boolean isAccountOpenedSuccessfully() {
        try {
            // BULLETPROOF SYNC: If the unique account ID physically appears in the DOM,
            // it is the absolute proof that the account was created successfully.
            // We bypass the flaky header text extraction entirely.
            wait.until(ExpectedConditions.visibilityOf(textNewAccountId));
            return true; 
        } catch (Exception e) {
            return false;
        }
    }

    public String getGeneratedAccountNumber() {
        return textNewAccountId.getText();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLogout)).click();
    }
}