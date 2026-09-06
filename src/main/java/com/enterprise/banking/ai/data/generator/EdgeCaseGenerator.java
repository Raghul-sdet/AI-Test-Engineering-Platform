package com.enterprise.banking.ai.data.generator;

/**
 * Specialized engine for generating anomalous operational data to trigger
 * unhandled exceptions or parsing errors in backend systems.
 */
public class EdgeCaseGenerator {

    /**
     * Generates a string composed entirely of whitespace characters.
     *
     * @return Whitespace string
     */
    public static String generateWhitespaceString() {
        return "       ";
    }

    /**
     * Generates a string utilizing multi-byte Unicode characters to validate encoding.
     *
     * @return Unicode string
     */
    public static String generateUnicodeString() {
        return "测试-こんにちは-안녕하세요";
    }

    /**
     * Generates emoji characters to test mobile-centric or rich text input boundaries.
     *
     * @return Emoji string
     */
    public static String generateEmojiString() {
        return "🏦💳💸";
    }

    /**
     * explicitly returns a literal null for missing data validation.
     *
     * @return null
     */
    public static Object generateNullValue() {
        return null;
    }
}