package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.exception.AiExtensionException;
import com.enterprise.banking.ai.model.TestScenario;
import com.enterprise.banking.ai.provider.AiProvider;
import com.enterprise.banking.ai.provider.OpenAiProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for communicating with the AI Engine to generate 
 * abstract Test Scenarios based on raw functional requirements.
 */
public class AiScenarioGeneratorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiScenarioGeneratorService.class);
    private final AiProvider aiProvider;

    /**
     * Default constructor initializing the standard Enterprise AI Provider.
     * Required for framework orchestration instantiation without manual dependency wiring.
     */
    public AiScenarioGeneratorService() {
        this.aiProvider = new OpenAiProvider();
    }

    /**
     * Parameterized constructor for Dependency Injection and isolated unit testing.
     *
     * @param aiProvider The AI engine integration provider (e.g., MockAiProvider).
     */
    public AiScenarioGeneratorService(AiProvider aiProvider) {
        this.aiProvider = aiProvider;
    }

    /**
     * Orchestrates the generation of abstract test scenarios from the given requirement text.
     * Interfaces with the AI Provider and parses the deterministic response into POJOs.
     *
     * @param requirementText The raw functional requirement or user story.
     * @return A list of generated and parsed Test Scenarios.
     * @throws AiExtensionException if the AI integration encounters an unrecoverable fault.
     */
    public List<TestScenario> generateScenarios(String requirementText) {
        if (requirementText == null || requirementText.trim().isEmpty()) {
            LOGGER.warn("Requirement text is null or empty. Bypassing scenario generation.");
            return new ArrayList<>();
        }

        LOGGER.info("Initiating AI-driven Test Scenario generation for requirement length: {} characters", requirementText.length());
        
        try {
            // Static system instruction to enforce structural output from the AI model
            String systemPrompt = "Analyze the provided requirement and generate deterministic test scenarios. "
                    + "Format exactly with SCENARIO_START and SCENARIO_END bounds. "
                    + "Include specific tags: TITLE:, DESCRIPTION:, CATEGORY:, PRIORITY:.";
            
            String aiResponse = aiProvider.generateResponse(systemPrompt, requirementText);
            
            if (aiResponse == null || aiResponse.trim().isEmpty()) {
                throw new AiExtensionException("AI Provider returned an empty response during scenario generation.");
            }

            List<TestScenario> parsedScenarios = parseScenarios(aiResponse, requirementText);
            
            if (parsedScenarios.isEmpty()) {
                 LOGGER.warn("AI generated response could not be parsed into valid Test Scenarios.");
            }
            
            LOGGER.info("Successfully synthesized {} abstract Test Scenarios.", parsedScenarios.size());
            return parsedScenarios;
            
        } catch (Exception exception) {
            LOGGER.error("Encountered critical failure during AI Test Scenario generation.", exception);
            throw new AiExtensionException("Failed to generate test scenarios via AI Provider", exception);
        }
    }

    /**
     * Parses the structured deterministic response from the AI Engine into POJO models.
     *
     * @param aiResponse      The raw text payload returned by the AI provider.
     * @param originalFeature The root requirement text used as a feature reference.
     * @return The list of structured TestScenario objects.
     */
    private List<TestScenario> parseScenarios(String aiResponse, String originalFeature) {
        List<TestScenario> parsedScenarios = new ArrayList<>();
        String[] blocks = aiResponse.split("SCENARIO_START");
        
        // Truncate the feature name to act as a summary reference
        String featureReference = originalFeature.length() > 50 
                ? originalFeature.substring(0, 50) + "..." 
                : originalFeature;
        
        for (String block : blocks) {
            if (block.trim().isEmpty() || !block.contains("SCENARIO_END")) {
                continue; // Skip invalid or incomplete generated blocks
            }
            
            String scenarioId = "SCN-AI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            TestScenario scenario = new TestScenario(
                    scenarioId,
                    featureReference,
                    extractField(block, "TITLE:", "DESCRIPTION:"),
                    extractField(block, "DESCRIPTION:", "CATEGORY:"),
                    extractField(block, "CATEGORY:", "PRIORITY:"),
                    extractField(block, "PRIORITY:", "SCENARIO_END")
            );
            
            parsedScenarios.add(scenario);
        }
        
        return parsedScenarios;
    }

    /**
     * Safely extracts string fields bounded by predefined deterministic tags.
     * Avoids regex processing overhead by utilizing rapid index lookups.
     *
     * @param source   The source text block payload.
     * @param startTag The beginning delimiter tag.
     * @param endTag   The ending delimiter tag.
     * @return The extracted and trimmed string value, or a fallback message if extraction fails.
     */
    private String extractField(String source, String startTag, String endTag) {
        try {
            int startIndex = source.indexOf(startTag);
            if (startIndex == -1) {
                return "N/A";
            }
            startIndex += startTag.length();
            
            int endIndex = source.indexOf(endTag, startIndex);
            if (endIndex == -1) {
                endIndex = source.length();
            }
            
            return source.substring(startIndex, endIndex).trim();
        } catch (Exception exception) {
            LOGGER.debug("Data extraction mapping failed for tag: {}", startTag, exception);
            return "Extraction Failed";
        }
    }
}