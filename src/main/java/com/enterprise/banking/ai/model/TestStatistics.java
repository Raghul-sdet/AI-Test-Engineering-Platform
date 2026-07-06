package com.enterprise.banking.ai.model;

import java.util.Map;

/**
 * Immutable record representing the aggregated health and coverage metrics
 * of the AI-generated Enterprise Test Plan.
 */
public record TestStatistics(
        int totalRequirements,
        int totalScenarios,
        int totalTestCases,
        int totalTestSteps,
        Map<String, Integer> categoryDistribution,
        Map<String, Integer> priorityDistribution,
        Map<String, Integer> severityDistribution
) {}