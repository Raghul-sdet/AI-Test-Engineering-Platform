package com.enterprise.banking.ai.analysis;

import com.enterprise.banking.ai.model.Requirement;
import com.enterprise.banking.ai.model.RequirementRisk;

import java.util.ArrayList;
import java.util.List;

/**
 * Scans requirement contexts to detect potential high, medium, and low impact risks.
 * Exposes classification utilities for cross-module compatibility.
 */
public class RiskAnalyzer {

    private static final String SEVERITY_CRITICAL = "CRITICAL";
    private static final String SEVERITY_HIGH = "HIGH";
    private static final String SEVERITY_MEDIUM = "MEDIUM";
    private static final String SEVERITY_LOW = "LOW";

    /**
     * Detects implementation and business risks associated with the requirement.
     *
     * @param requirement The structured requirement object to analyze
     * @return List of identified risks with proposed mitigations
     */
    public List<RequirementRisk> detectRisks(Requirement requirement) {
        List<RequirementRisk> detectedRisks = new ArrayList<>();
        String content = requirement.getDescription().toLowerCase();

        evaluateSecurityRisks(content, detectedRisks);
        evaluateFinancialRisks(content, detectedRisks);
        evaluateRegressionRisks(content, detectedRisks);
        
        if (detectedRisks.isEmpty()) {
            detectedRisks.add(new RequirementRisk(
                    "Operational Implementation",
                    SEVERITY_LOW,
                    "Standard deployment considerations apply.",
                    "Execute standard CI/CD pipeline and monitoring."
            ));
        }

        return detectedRisks;
    }

    /**
     * Calculates the priority level based on risk keywords in the provided text.
     * Exposed for compatibility with ScenarioClassifier.
     *
     * @param text The text to evaluate
     * @return The calculated priority string
     */
    public String calculatePriority(String text) {
        if (text == null || text.trim().isEmpty()) {
            return SEVERITY_LOW;
        }
        
        String lowerText = text.toLowerCase();
        if (lowerText.contains("blocker") || lowerText.contains("urgent") || lowerText.contains("critical")) {
            return SEVERITY_CRITICAL;
        } else if (lowerText.contains("high") || lowerText.contains("priority 1")) {
            return SEVERITY_HIGH;
        } else if (lowerText.contains("medium")) {
            return SEVERITY_MEDIUM;
        }
        
        return SEVERITY_LOW;
    }

    /**
     * Calculates the severity level based on risk keywords in the provided text.
     * Exposed for compatibility with ScenarioClassifier.
     *
     * @param text The text to evaluate
     * @return The calculated severity string
     */
    public String calculateSeverity(String text) {
        if (text == null || text.trim().isEmpty()) {
            return SEVERITY_LOW;
        }

        String lowerText = text.toLowerCase();
        if (lowerText.contains("data loss") || lowerText.contains("security breach") || lowerText.contains("outage")) {
            return SEVERITY_CRITICAL;
        } else if (lowerText.contains("financial") || lowerText.contains("ledger") || lowerText.contains("high impact")) {
            return SEVERITY_HIGH;
        } else if (lowerText.contains("regression") || lowerText.contains("workflow")) {
            return SEVERITY_MEDIUM;
        }
        
        return SEVERITY_LOW;
    }

    private void evaluateSecurityRisks(String content, List<RequirementRisk> risks) {
        // "secur" catches secure/security/securely; "auth" catches authenticate/
        // authentication/authorize/authorization. Matching only the exact words
        // "security"/"authentication" missed common phrasing like "authenticate securely".
        if (content.contains("secur") || content.contains("auth") || content.contains("password")) {
            risks.add(new RequirementRisk(
                    "Security Impact",
                    SEVERITY_CRITICAL,
                    "Changes affect authentication or security protocols.",
                    "Enforce strict OWASP validation, penetration testing, and token encryption."
            ));
        }
    }

    private void evaluateFinancialRisks(String content, List<RequirementRisk> risks) {
        if (content.contains("payment") || content.contains("transaction") || content.contains("ledger")) {
            risks.add(new RequirementRisk(
                    "Financial Transaction Risk",
                    SEVERITY_HIGH,
                    "Potential for financial calculation mismatch or transaction failure.",
                    "Implement transactional rollbacks and verify ledger integrity."
            ));
        }
    }

    private void evaluateRegressionRisks(String content, List<RequirementRisk> risks) {
        if (content.contains("update") || content.contains("modify") || content.contains("legacy")) {
            risks.add(new RequirementRisk(
                    "Regression Impact",
                    SEVERITY_MEDIUM,
                    "Modification of existing business rules could break legacy workflows.",
                    "Execute full automated regression test suite prior to release."
            ));
        }
    }
}