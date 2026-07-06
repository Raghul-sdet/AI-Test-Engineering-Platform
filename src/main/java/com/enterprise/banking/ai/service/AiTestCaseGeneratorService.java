package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestScenario;
import com.enterprise.banking.ai.exception.AiExtensionException;
import com.enterprise.banking.ai.provider.AiProvider;
import com.enterprise.banking.ai.provider.OpenAiProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service responsible for communicating with the AI Engine to generate 
 * concrete Test Cases and execution steps based on abstract Test Scenarios.
 */
public class AiTestCaseGeneratorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiTestCaseGeneratorService.class);
    private final AiProvider aiProvider;

    /**
     * Default constructor initializing the standard Enterprise AI Provider.
     */
    public AiTestCaseGeneratorService() {
        this.aiProvider = new OpenAiProvider();
    }

    /**
     * Parameterized constructor for Dependency Injection.
     * Applies strict fail-fast null safety checks.
     *
     * @param aiProvider The AI engine integration provider.
     */
    public AiTestCaseGeneratorService(AiProvider aiProvider) {
        this.aiProvider = Objects.requireNonNull(aiProvider, "AiProvider implementation cannot be null.");
    }

    /**
     * Orchestrates the generation of concrete test cases from the given scenarios.
     *
     * @param scenarios The list of abstract test scenarios.
     * @return A consolidated list of generated and parsed Test Cases.
     */
    public List<TestCase> generateTestCasesWithSteps(List<TestScenario> scenarios) {
        if (scenarios == null || scenarios.isEmpty()) {
            LOGGER.warn("Test scenarios collection is null or empty. Bypassing generation.");
            // Optimized: Returns an immutable empty list avoiding object creation
            return Collections.emptyList();
        }

        LOGGER.info("Initiating AI-driven Test Case and Step generation for {} scenarios.", scenarios.size());
        List<TestCase> consolidatedTestCases = new ArrayList<>();

        for (TestScenario scenario : scenarios) {
            try {
                LOGGER.debug("Transmitting scenario context to AI Provider. Scenario ID: {}", scenario.getScenarioId());
                
                String systemPrompt = "Analyze the scenario and generate deterministic test cases. Format exactly with TEST_CASE_START and TEST_CASE_END bounds. Include tags: TITLE:, OBJECTIVE:, PRECONDITION:, PRIORITY:, SEVERITY:, STEPS:.";
                String aiResponse = aiProvider.generateResponse(systemPrompt, scenario.getScenarioDescription());
                
                if (aiResponse == null || aiResponse.trim().isEmpty()) {
                    throw new AiExtensionException("AI Provider returned an empty response for scenario: " + scenario.getScenarioId());
                }

                List<TestCase> parsedTestCases = parseTestCases(aiResponse, scenario.getScenarioId());
                
                if (parsedTestCases.isEmpty()) {
                     LOGGER.warn("AI generated response could not be parsed into valid Test Cases for scenario: {}", scenario.getScenarioId());
                }
                
                scenario.setTestCases(parsedTestCases);
                consolidatedTestCases.addAll(parsedTestCases);
                
            } catch (Exception exception) {
                LOGGER.error("Encountered failure during AI Test Case generation for scenario ID: {}", scenario.getScenarioId(), exception);
                throw new AiExtensionException("Failed to generate test cases via AI Provider", exception);
            }
        }

        LOGGER.info("Successfully synthesized {} complete Test Cases.", consolidatedTestCases.size());
        return consolidatedTestCases;
    }

    /**
     * Parses the structured deterministic response from the AI Engine into POJO models.
     *
     * @param aiResponse The raw text payload returned by the AI provider.
     * @param scenarioId The parent scenario identifier.
     * @return The list of structured TestCase objects.
     */
    private List<TestCase> parseTestCases(String aiResponse, String scenarioId) {
        if (aiResponse == null) return Collections.emptyList();

        List<TestCase> parsedCases = new ArrayList<>();
        String[] blocks = aiResponse.split("TEST_CASE_START");
        
        for (String block : blocks) {
            if (block.trim().isEmpty() || !block.contains("TEST_CASE_END")) {
                continue;
            }
            
            String caseId = "TC-AI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            TestCase testCase = new TestCase(
                    caseId,
                    scenarioId,
                    extractField(block, "TITLE:", "OBJECTIVE:"),
                    extractField(block, "OBJECTIVE:", "PRECONDITION:"),
                    extractField(block, "PRECONDITION:", "PRIORITY:"),
                    extractField(block, "PRIORITY:", "SEVERITY:"),
                    extractField(block, "SEVERITY:", "STEPS:"),
                    Collections.emptyList() // Safe immutable list initialization
            );
            
            testCase.setTestSteps(extractField(block, "STEPS:", "TEST_CASE_END"));
            testCase.setStatus("GENERATED");
            parsedCases.add(testCase);
        }
        
        return parsedCases;
    }

    /**
     * Safely extracts string fields bounded by predefined deterministic tags.
     *
     * @param source   The source text payload.
     * @param startTag The beginning delimiter tag.
     * @param endTag   The ending delimiter tag.
     * @return The extracted string value, or a fallback message.
     */
    private String extractField(String source, String startTag, String endTag) {
        try {
            int startIndex = source.indexOf(startTag);
            if (startIndex == -1) return "N/A";
            
            startIndex += startTag.length();
            int endIndex = source.indexOf(endTag, startIndex);
            if (endIndex == -1) endIndex = source.length();
            
            return source.substring(startIndex, endIndex).trim();
        } catch (Exception exception) {
            LOGGER.debug("Data extraction mapping failed for tag: {}", startTag, exception);
            return "Extraction Failed";
        }
    }
}