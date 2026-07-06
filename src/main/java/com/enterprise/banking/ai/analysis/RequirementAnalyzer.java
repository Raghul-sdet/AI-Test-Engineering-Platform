package com.enterprise.banking.ai.analysis;

import com.enterprise.banking.ai.provider.AiProvider;

public class RequirementAnalyzer {

    private final AiProvider aiProvider;

    public RequirementAnalyzer(AiProvider aiProvider) {
        this.aiProvider = aiProvider;
    }

    public String generateComprehensiveQaDesign(String requirement) {
        System.out.println("[REQUIREMENT-ENGINE] Engineering AI Prompt for Requirement: " + requirement);
        
        String systemPrompt = "You are a Senior QA Lead with 15+ years of enterprise testing experience. Analyze the requirement: '" + requirement + "'.\n" +
                "CRITICAL DIRECTIVE: You MUST generate between 15 and 25 distinct Test Scenarios. DO NOT generate only one.\n" +
                "CRITICAL DIRECTIVE: Every Scenario MUST contain between 5 and 10 Test Cases.\n" +
                "CRITICAL DIRECTIVE: Every Test Case MUST contain between 3 and 10 Test Steps.\n" +
                "You MUST cover ALL the following categories: Functional, Negative, Boundary, Security, Performance, Accessibility, Integration, Regression, Compliance.\n" +
                "Generate strictly valid JSON matching this schema. Do NOT include markdown blocks:\n" +
                "{\n" +
                "  \"requirementId\": \"REQ-001\",\n" +
                "  \"featureName\": \"" + requirement + "\",\n" +
                "  \"scenarios\": [\n" +
                "    {\n" +
                "      \"scenarioId\": \"SC-01\",\n" +
                "      \"category\": \"Functional\",\n" +
                "      \"title\": \"Verify successful transfer\",\n" +
                "      \"testCases\": [\n" +
                "        {\n" +
                "          \"testCaseId\": \"TC-01\",\n" +
                "          \"title\": \"Valid cross-border transfer\",\n" +
                "          \"objective\": \"Ensure funds deduct and credit correctly\",\n" +
                "          \"precondition\": \"User is logged in with active balance\",\n" +
                "          \"steps\": [\n" +
                "            {\"stepNum\": 1, \"action\": \"Enter valid IBAN\", \"testData\": \"IBAN=GB123\", \"expected\": \"IBAN accepted\"}\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        
        return aiProvider.executeCompletion(systemPrompt, requirement);
    }
}