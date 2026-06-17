package com.enterprise.banking.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomDataGenerator {

    /**
     * Generates a unique, strictly alphanumeric username to bypass Parabank validation errors.
     * @return A unique random username string.
     */
    public static String generateRandomUsername() {
        String timestamp = new SimpleDateFormat("MMddHHmmss").format(new Date());
        return "QAUser" + timestamp;
    }

    /**
     * Generates a secure, unique password.
     * @return A unique random password string.
     */
    public static String generateRandomPassword() {
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        return "TestPass@" + timestamp;
    }
}