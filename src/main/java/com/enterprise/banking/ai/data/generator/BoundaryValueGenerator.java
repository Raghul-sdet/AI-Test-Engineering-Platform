package com.enterprise.banking.ai.data.generator;

/**
 * Specialized engine focused on generating mathematical and programmatic edge bounds
 * for boundary value testing.
 */
public class BoundaryValueGenerator {

    /**
     * Provides extreme maximum string lengths to test database field constraints.
     *
     * @return 255 character string
     */
    public static String generateMaximumLengthString() {
        return RandomDataEngine.generateRandomString(255);
    }

    /**
     * Provides the absolute minimum valid string.
     *
     * @return 1 character string
     */
    public static String generateMinimumLengthString() {
        return RandomDataEngine.generateRandomString(1);
    }

    /**
     * Provides extreme maximum values for transactional limits.
     *
     * @return High double value
     */
    public static double generateMaximumTransactionAmount() {
        return 9999999.99;
    }

    /**
     * Provides absolute minimum values for transactional logic.
     *
     * @return Valid boundary integer
     */
    public static int generateAbsoluteMinimumInteger() {
        return 1;
    }

    /**
     * Provides a strict zero boundary.
     *
     * @return Double zero
     */
    public static double generateZeroValue() {
        return 0.0;
    }
}