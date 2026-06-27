package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.RegistrationPage;
import com.enterprise.banking.repositories.UserRepository;
import com.enterprise.banking.utils.RandomDataGenerator;

public class RegistrationTest extends BaseTest {

    @Test
    public void verifyUserRegistrationAndDataStorage() {
        
        // Step 1: Generate dynamic test data
        String newUsername = RandomDataGenerator.generateRandomUsername();
        String newPassword = RandomDataGenerator.generateRandomPassword();

        System.out.println(">>> Attempting registration with Username: " + newUsername);

        // Step 2: Initialize Page Object
        RegistrationPage registrationPage = new RegistrationPage(getDriver());

        // Step 3: Execute Registration UI Workflow
        registrationPage.navigateToRegistration();
        registrationPage.fillRegistrationForm(newUsername, newPassword);
        registrationPage.clickSubmitRegistration();

        // Step 4: Strict Validation
        String actualSuccessMessage = registrationPage.getRegistrationSuccessMessage();
        Assert.assertTrue(actualSuccessMessage.contains("Welcome"), 
                "Assertion Failed: Registration success message did not display the expected text.");

        // Step 5: Save verified credentials directly to the H2 Database
        UserRepository.saveUser(newUsername, newPassword);
        System.out.println(">>> H2 Database Save Complete.");
        
        System.out.println(">>> Execution Complete. Next suite run will automatically test user: " + newUsername);
    }
}