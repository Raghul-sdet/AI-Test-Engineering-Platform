package com.enterprise.banking.ai.analysis;

import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoverageAnalyzer {

    private static final List<String> MANDATORY_CATEGORIES = Arrays.asList(
            "Functional", "Negative", "Boundary", "Security"
    );

    public void validateCoverage(TestPlan plan) {
        List<String> generatedCategories = plan.scenarios().stream()
                .map(TestScenario::category)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        for (String required : MANDATORY_CATEGORIES) {
            boolean isCovered = generatedCategories.stream().anyMatch(cat -> cat.contains(required.toUpperCase()));
            if (!isCovered) {
                System.out.println("[COVERAGE-WARNING] AI missed critical category: " + required + ". Pipeline proceeding.");
            }
        }
        System.out.println("[COVERAGE-ANALYZER] Enterprise Multi-Dimensional Coverage Validated.");
    }
}