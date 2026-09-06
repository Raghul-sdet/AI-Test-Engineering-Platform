package com.enterprise.banking.ai.healing.engine;

/**
 * Utility engine calculating the textual similarity between DOM attributes
 * to determine confidence scores for potential fallback locators.
 */
public class SimilarityEngine {

    /**
     * Calculates a similarity score (0.0 to 100.0) between two strings based on Levenshtein distance.
     *
     * @param original Target string
     * @param compared String to compare
     * @return Confidence score as a percentage
     */
    public double calculateSimilarityScore(String original, String compared) {
        if (original == null || compared == null) {
            return 0.0;
        }
        if (original.equals(compared)) {
            return 100.0;
        }
        
        int distance = computeLevenshteinDistance(original.toLowerCase(), compared.toLowerCase());
        int maxLength = Math.max(original.length(), compared.length());
        
        if (maxLength == 0) return 100.0;
        return (double) (maxLength - distance) / maxLength * 100.0;
    }

    private int computeLevenshteinDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }
}