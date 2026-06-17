package com.enterprise.banking.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage {

    private WebDriverWait wait;

    // --- Locators ---
    @FindBy(linkText = "Register")
    private WebElement linkRegister;

    @FindBy(id = "customer.firstName")
    private WebElement inputFirstName;

    @FindBy(id = "customer.lastName")
    private WebElement inputLastName;

    @FindBy(id = "customer.address.street")
    private WebElement inputStreet;

    @FindBy(id = "customer.address.city")
    private WebElement inputCity;

    @FindBy(id = "customer.address.state")
    private WebElement inputState;

    @FindBy(id = "customer.address.zipCode")
    private WebElement inputZipCode;

    @FindBy(id = "customer.phoneNumber")
    private WebElement inputPhoneNumber;

    @FindBy(id = "customer.ssn")
    private WebElement inputSsn;

    @FindBy(id = "customer.username")
    private WebElement inputUsername;

    @FindBy(id = "customer.password")
    private WebElement inputPassword;

    @FindBy(id = "repeatedPassword")
    private WebElement inputConfirmPassword;

    @FindBy(xpath = "//input[@value='Register']")
    private WebElement btnRegisterSubmit;

    @FindBy(xpath = "//div[@id='rightPanel']/h1")
    private WebElement textSuccessHeader;

    // Constructor
    public RegistrationPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // --- Methods ---
    public void navigateToRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(linkRegister)).click();
    }

    public void fillRegistrationForm(String username, String password) {
        wait.until(ExpectedConditions.visibilityOf(inputFirstName)).sendKeys("QA");
        inputLastName.sendKeys("Automation");
        inputStreet.sendKeys("123 Testing Blvd");
        inputCity.sendKeys("Tech City");
        inputState.sendKeys("CA");
        inputZipCode.sendKeys("90210");
        inputPhoneNumber.sendKeys("555-0199");
        inputSsn.sendKeys("000-11-2222");
        
        // Injecting the dynamic credentials
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        inputConfirmPassword.sendKeys(password);
    }

    public void clickSubmitRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(btnRegisterSubmit)).click();
    }

    public String getRegistrationSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOf(textSuccessHeader));
        return textSuccessHeader.getText();
    }
}