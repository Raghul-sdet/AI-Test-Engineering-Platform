package com.enterprise.banking.ai.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Analyzes action statements and test data to infer necessary method arguments.
 */
public class ParameterResolver {

    /**
     * Resolves the required parameters for a given test step.
     *
     * @param actionString The action step from Excel.
     * @param testData     The associated test data from Excel.
     * @return A list of inferred parameter names (e.g., ["username"]).
     */
    public List<String> resolveParameters(String actionString, String testData) {
        List<String> parameters = new ArrayList<>();
        String actionLower = actionString.toLowerCase(Locale.ROOT);
        
        boolean requiresInput = actionLower.contains("enter") || actionLower.contains("type") || actionLower.contains("input");
        
        if (requiresInput && testData != null && !testData.isBlank()) {
            // Simplified parsing for Phase 8: Extracting parameter name from standard format "Key=Value" or fallback.
            if (testData.contains("=")) {
                String key = testData.split("=")[0].trim().toLowerCase(Locale.ROOT);
                parameters.add(toCamelCase(key));
            } else {
                parameters.add("inputValue");
            }
        }
        
        return parameters;
    }
    
    public String determineActionType(String actionString) {
        String lower = actionString.toLowerCase(Locale.ROOT);
        if (lower.contains("enter") || lower.contains("type")) return "INPUT";
        if (lower.contains("verify") || lower.contains("check")) return "ASSERT";
        return "CLICK";
    }
    
    private String toCamelCase(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }
}