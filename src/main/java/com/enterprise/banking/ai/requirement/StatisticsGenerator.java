package com.enterprise.banking.ai.requirement;

import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;
import com.enterprise.banking.ai.model.TestStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Traverses the TestPlan object graph to aggregate enterprise QA metrics.
 */
public class StatisticsGenerator {

    public TestStatistics calculateMetrics(TestPlan plan) {
        int totalScenarios = plan.scenarios().size();
        int totalCases = 0;
        int totalSteps = 0;

        Map<String, Integer> categoryDist = new HashMap<>();
        Map<String, Integer> priorityDist = new HashMap<>();
        Map<String, Integer> severityDist = new HashMap<>();

        for (TestScenario scenario : plan.scenarios()) {
            // Aggregate Categories
            categoryDist.merge(scenario.category(), 1, Integer::sum);

            List<TestCase> cases = plan.testCasesByScenarioId().getOrDefault(scenario.id(), List.of());
            totalCases += cases.size();

            for (TestCase tc : cases) {
                totalSteps += tc.steps().size();
                
                // Aggregate Priorities and Severities
                priorityDist.merge(tc.priority(), 1, Integer::sum);
                severityDist.merge(tc.severity(), 1, Integer::sum);
            }
        }

        return new TestStatistics(
                1, // Currently 1 requirement per run
                totalScenarios,
                totalCases,
                totalSteps,
                categoryDist,
                priorityDist,
                severityDist
        );
    }
}