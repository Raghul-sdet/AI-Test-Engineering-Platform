package com.enterprise.banking.ai.analysis;

public class RiskAnalyzer {

    public String calculatePriority(String category) {
        if (category == null) return "Low";
        String upperCat = category.toUpperCase();
        if (upperCat.contains("SECURITY") || upperCat.contains("PERFORMANCE") || upperCat.contains("NEGATIVE") || upperCat.contains("BOUNDARY")) {
            return "High";
        } else if (upperCat.contains("FUNCTIONAL") || upperCat.contains("INTEGRATION") || upperCat.contains("DATA")) {
            return "Medium";
        }
        return "Low";
    }

    public String calculateSeverity(String category) {
        if (category == null) return "Minor";
        String upperCat = category.toUpperCase();
        if (upperCat.contains("SECURITY") || upperCat.contains("BOUNDARY")) {
            return "Critical";
        } else if (upperCat.contains("FUNCTIONAL")) {
            return "Major";
        }
        return "Minor";
    }
}