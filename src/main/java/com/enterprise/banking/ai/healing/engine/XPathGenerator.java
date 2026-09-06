package com.enterprise.banking.ai.healing.engine;

/**
 * Intelligent utility for synthesizing fallback XPath expressions dynamically.
 */
public class XPathGenerator {

    public static String generateByAttribute(String tag, String attribute, String value) {
        String safeTag = (tag != null && !tag.isEmpty()) ? tag : "*";
        return String.format("//%s[@%s='%s']", safeTag, attribute, value);
    }

    public static String generateByPartialAttribute(String tag, String attribute, String partialValue) {
        String safeTag = (tag != null && !tag.isEmpty()) ? tag : "*";
        return String.format("//%s[contains(@%s, '%s')]", safeTag, attribute, partialValue);
    }

    public static String generateByText(String tag, String text) {
        String safeTag = (tag != null && !tag.isEmpty()) ? tag : "*";
        return String.format("//%s[text()='%s']", safeTag, text);
    }
}