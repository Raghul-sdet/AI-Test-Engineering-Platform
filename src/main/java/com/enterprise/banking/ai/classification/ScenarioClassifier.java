package com.enterprise.banking.ai.classification;

import com.enterprise.banking.ai.analysis.RiskAnalyzer;
import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;
import com.enterprise.banking.ai.model.TestStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScenarioClassifier {

    private final RiskAnalyzer riskAnalyzer = new RiskAnalyzer();
    private final ObjectMapper mapper = new ObjectMapper();

    public TestPlan parseAiDesign(String jsonPayload) throws Exception {
        JsonNode root = mapper.readTree(jsonPayload);
        
        // Fixed: Removed deprecated asText(String defaultValue)
        String reqId = root.path("requirementId").isMissingNode() ? "REQ-UNKNOWN" : root.path("requirementId").asText();
        String featureName = root.path("featureName").isMissingNode() ? "Unknown Feature" : root.path("featureName").asText();
        
        List<TestScenario> scenarios = new ArrayList<>();
        Map<String, List<TestCase>> testCasesMap = new LinkedHashMap<>();

        JsonNode scenariosArray = root.path("scenarios");
        for (JsonNode sNode : scenariosArray) {
            String category = sNode.path("category").asText();
            String priority = riskAnalyzer.calculatePriority(category);
            
            TestScenario scenario = new TestScenario(
                    sNode.path("scenarioId").asText(),
                    reqId,
                    featureName,
                    sNode.path("title").asText(),
                    category,
                    priority
            );
            scenarios.add(scenario);
            
            List<TestCase> cases = new ArrayList<>();
            JsonNode casesArray = sNode.path("testCases");
            
            for (JsonNode cNode : casesArray) {
                List<TestStep> steps = new ArrayList<>();
                JsonNode stepsArray = cNode.path("steps");
                
                for (JsonNode stepNode : stepsArray) {
                    // Fixed: Handled deprecation inside steps as well
                    String testData = stepNode.path("testData").isMissingNode() ? "" : stepNode.path("testData").asText();
                    steps.add(new TestStep(
                            stepNode.path("stepNum").asInt(),
                            stepNode.path("action").asText(),
                            testData,
                            stepNode.path("expected").asText()
                    ));
                }
                
                String precond = cNode.path("precondition").isMissingNode() ? "" : cNode.path("precondition").asText();
                
                cases.add(new TestCase(
                        cNode.path("testCaseId").asText(),
                        scenario.id(),
                        cNode.path("title").asText(),
                        cNode.path("objective").asText(),
                        precond,
                        priority,
                        riskAnalyzer.calculateSeverity(category),
                        steps
                ));
            }
            testCasesMap.put(scenario.id(), cases);
        }
        
        return new TestPlan(reqId, featureName, scenarios, testCasesMap);
    }
}