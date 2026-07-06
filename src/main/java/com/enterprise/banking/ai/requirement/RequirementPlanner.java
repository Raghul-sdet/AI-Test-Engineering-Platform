package com.enterprise.banking.ai.requirement;

import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.provider.AiProvider;
import com.enterprise.banking.ai.prompt.PromptTemplates;
import com.enterprise.banking.ai.exception.AiExtensionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Responsible for analyzing raw business requirements and generating a structured TestPlan
 * by interfacing with the configured AI Provider. Follows Single Responsibility Principle (SRP).
 */
public class RequirementPlanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequirementPlanner.class);
    
    private final AiProvider aiProvider;

    /**
     * Constructs the RequirementPlanner with the necessary AI Provider.
     * Follows Dependency Injection to ensure modularity and easy mock testing.
     *
     * @param aiProvider The provider implementation (e.g., OpenAiProvider) used to analyze requirements.
     */
    public RequirementPlanner(AiProvider aiProvider) {
        this.aiProvider = Objects.requireNonNull(aiProvider, "AiProvider cannot be null");
    }

    /**
     * Analyzes the given raw requirement text and produces a foundational TestPlan.
     *
     * @param requirementText The raw functional requirement or user story.
     * @return A structured TestPlan object containing the analysis context.
     * @throws AiExtensionException if the requirement is invalid or AI processing fails.
     */
    public TestPlan analyzeRequirement(String requirementText) {
        if (requirementText == null || requirementText.trim().isEmpty()) {
            LOGGER.error("Requirement text provided for analysis is null or empty");
            throw new IllegalArgumentException("Requirement text cannot be null or empty");
        }

        LOGGER.info("Starting requirement analysis for input length: {} characters", requirementText.length());

        try {
            // Retrieve the enterprise-standard prompt for requirement parsing
            String systemPrompt = PromptTemplates.getRequirementAnalysisPrompt();
            
            LOGGER.debug("Transmitting requirement to AI Provider for context extraction");
            String aiAnalysisResult = aiProvider.generateResponse(systemPrompt, requirementText);
            
            if (aiAnalysisResult == null || aiAnalysisResult.trim().isEmpty()) {
                 throw new AiExtensionException("AI Provider returned an empty response during requirement analysis.");
            }

            return buildTestPlan(requirementText, aiAnalysisResult);

        } catch (Exception exception) {
            LOGGER.error("Failed to analyze requirement: {}", exception.getMessage(), exception);
            throw new AiExtensionException("Requirement analysis process failed due to internal error", exception);
        }
    }

    /**
     * Constructs the TestPlan entity using the raw requirement and AI analysis result.
     *
     * @param originalRequirement The original text provided by the user.
     * @param aiExtractedContext  The structured data or context extracted by the AI.
     * @return A fully initialized TestPlan.
     */
    private TestPlan buildTestPlan(String originalRequirement, String aiExtractedContext) {
        TestPlan plan = new TestPlan();
        
        // Simulating the standard Enterprise setters for a TestPlan model
        plan.setPlanId(UUID.randomUUID().toString());
        plan.setOriginalRequirement(originalRequirement);
        plan.setAiContext(aiExtractedContext);
        plan.setCreationTimestamp(LocalDateTime.now());
        plan.setPlanStatus("ANALYZED");
        
        LOGGER.info("Successfully generated TestPlan with ID: {}", plan.getPlanId());
        
        return plan;
    }
}