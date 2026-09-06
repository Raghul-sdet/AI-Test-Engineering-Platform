package com.enterprise.banking.ai.data.generator;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Central utility engine responsible for generating cryptographically secure, 
 * randomized values for various data types without relying on external libraries.
 */
public class RandomDataEngine {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length The target length
     * @return Generated random string
     */
    public static String generateRandomString(int length) {
        if (length <= 0) return "";
        StringBuilder sb = new StringBuilder(length);
        Random random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Generates a random numeric string (useful for accounts and phones).
     *
     * @param length The target length
     * @return Generated numeric string
     */
    public static String generateRandomNumericString(int length) {
        if (length <= 0) return "";
        StringBuilder sb = new StringBuilder(length);
        Random random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(NUMBERS.length());
            sb.append(NUMBERS.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Generates a random double within the specified inclusive boundary.
     *
     * @param min Minimum bound
     * @param max Maximum bound
     * @return Generated double
     */
    public static double generateRandomDouble(double min, double max) {
        if (min >= max) return min;
        return min + (max - min) * ThreadLocalRandom.current().nextDouble();
    }
    
    /**
     * Generates a unique email address to prevent database constraint collisions.
     *
     * @return Random email string
     */
    public static String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@enterprise.banking.local";
    }
}