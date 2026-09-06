package com.enterprise.banking.ai.healing.engine;

/**
 * Intelligent utility for synthesizing fallback CSS Selector expressions dynamically.
 */
public class CssSelectorGenerator {

    public static String generateById(String tag, String id) {
        String safeTag = (tag != null && !tag.isEmpty()) ? tag : "";
        return String.format("%s#%s", safeTag, id);
    }

    public static String generateByClass(String tag, String className) {
        String safeTag = (tag != null && !tag.isEmpty()) ? tag : "";
        String safeClass = className.trim().replace(" ", ".");
        return String.format("%s.%s", safeTag, safeClass);
    }

    public static String generateByAttribute(String tag, String attribute, String value) {
        String safeTag = (tag != null && !tag.isEmpty()) ? tag : "";
        return String.format("%s[%s='%s']", safeTag, attribute, value);
    }
}