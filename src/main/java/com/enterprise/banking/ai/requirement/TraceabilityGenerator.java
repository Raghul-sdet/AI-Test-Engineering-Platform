package com.enterprise.banking.ai.requirement;

import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TraceabilityGenerator {

    public List<Map<String, String>> buildTraceabilityMatrix(TestPlan plan) {
        List<Map<String, String>> matrix = new ArrayList<>();
        String reqId = plan.requirementId();

        for (TestScenario scenario : plan.scenarios()) {
            List<TestCase> cases = plan.testCasesByScenarioId().get(scenario.id());
            if (cases != null) {
                for (TestCase testCase : cases) {
                    Map<String, String> row = new LinkedHashMap<>();
                    row.put("Requirement ID", reqId);
                    row.put("Scenario ID", scenario.id());
                    row.put("Test Case ID", testCase.testCaseId());
                    row.put("Category", scenario.category());
                    row.put("Priority", testCase.priority());
                    row.put("Coverage Status", "Covered");
                    matrix.add(row);
                }
            }
        }
        return matrix;
    }
}