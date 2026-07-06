package com.enterprise.banking.ai.prompt;

/**
 * Centralized utility repository for maintaining AI system prompts.
 * Separating prompt definitions from business logic adheres to the Single Responsibility Principle,
 * ensuring prompts can be version-controlled, tuned, and localized independently.
 */
public final class PromptTemplates {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Enforces the enterprise standard of accessing prompts statically.
     */
    private PromptTemplates() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Retrieves the enterprise-standard prompt required for analyzing raw functional requirements.
     * Guides the AI to extract structured data necessary for constructing the TestPlan model.
     *
     * @return The strict system prompt string.
     */
    public static String getRequirementAnalysisPrompt() {
        return """
               You are an Enterprise QA Automation Architect. 
               Analyze the provided business requirement and extract the core testing context.
               Provide a structured analysis focusing on the feature scope, target audience, 
               and critical functional boundaries.
               Ensure the response is deterministic and strictly analytical. Do not generate test cases at this stage.
               """;
    }
}