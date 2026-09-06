package com.enterprise.banking.ai.data.generator;

/**
 * Specialized engine focused on generating malicious, malformed, and explicitly
 * incorrect data designed to trigger error handling and security block logic.
 */
public class NegativeDataGenerator {

    /**
     * Generates known SQL injection payloads to validate security defenses.
     *
     * @return SQLi String
     */
    public static String generateSqlInjectionString() {
        return "' OR '1'='1' -- ";
    }

    /**
     * Generates Cross-Site Scripting (XSS) payloads to validate input sanitization.
     *
     * @return XSS String
     */
    public static String generateXssPayload() {
        return "<script>alert('XSS')</script>";
    }

    /**
     * Generates explicitly invalid email structures.
     *
     * @return Malformed email
     */
    public static String generateInvalidEmail() {
        return "invalid-email-at-domain.com";
    }

    /**
     * Generates negative numeric amounts, strictly invalid in banking transfers.
     *
     * @return Negative double
     */
    public static double generateNegativeAmount() {
        return -500.00;
    }

    /**
     * Generates strings containing special characters often restricted in names/addresses.
     *
     * @return Special character string
     */
    public static String generateSpecialCharacters() {
        return "!@#$%^&*()_+{}|:\"<>?";
    }
}