package com.enterprise.banking.ai.healing.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Simulates AI inspection of the DOM. In a live Selenium integration, this reads
 * the Javascript executor DOM state to find neighboring nodes for broken locators.
 */
public class DOMComparator {

    /**
     * Parses a raw DOM node string into a map of attributes for analysis.
     *
     * @param domNode The raw HTML string representing the target element
     * @return Map of attributes (e.g., id, name, class)
     */
    public Map<String, String> extractAttributes(String domNode) {
        Map<String, String> attributes = new HashMap<>();
        if (domNode == null || domNode.isEmpty()) return attributes;

        // Note: Simple mock parser for enterprise standard demonstration.
        // Assumes structure like <tag id="val" class="val">
        String[] parts = domNode.split(" ");
        for (String part : parts) {
            if (part.contains("=")) {
                String[] keyVal = part.split("=");
                if (keyVal.length == 2) {
                    String key = keyVal[0].trim();
                    String value = keyVal[1].replace("\"", "").replace(">", "").trim();
                    attributes.put(key, value);
                }
            }
        }
        return attributes;
    }
}