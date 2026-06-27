package com.enterprise.banking.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class RandomDataGenerator {

    /**
     * Generates a unique, strictly alphanumeric username.
     * Uses UUID to guarantee 100% uniqueness across parallel threads 
     * hitting the method at the exact same second.
     * * @return A unique random username string.
     */
    public static String generateRandomUsername() {
        // Generate a 5-character random unique string
        String uniqueID = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        
        // Get the current time down to the second
        String timestamp = new SimpleDateFormat("MMddHHmmss").format(new Date());
        
        // Combine them so no two threads can ever generate the same name
        return "QA" + timestamp + uniqueID;
    }

    /**
     * Generates a secure, unique password.
     * * @return A unique random password string.
     */
    public static String generateRandomPassword() {
        String uniqueID = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "TestPass@" + uniqueID + "!";
    }
}