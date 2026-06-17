package com.enterprise.banking.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountOverviewPage {

    private WebDriverWait wait;

    @FindBy(xpath = "//h1[contains(text(),'Accounts Overview')]")
    private WebElement pageHeader;

    @FindBy(xpath = "//*[@id='accountTable']//tbody/tr[1]/td[2]")
    private WebElement textAccountBalance;

    @FindBy(linkText = "Log Out")
    private WebElement btnLogout;

    public AccountOverviewPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isHeaderDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(pageHeader));
        return pageHeader.isDisplayed();
    }

    public String getAccountBalance() {
        wait.until(ExpectedConditions.visibilityOf(textAccountBalance));
        return textAccountBalance.getText();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLogout));
        btnLogout.click();
    }
}