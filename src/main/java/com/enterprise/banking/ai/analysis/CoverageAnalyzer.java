package com.enterprise.banking.ai.analysis;

import com.enterprise.banking.ai.model.Requirement;
import com.enterprise.banking.ai.model.RequirementCoverage;

/**
 * Calculates testing and business coverage metrics for a formalized requirement.
 */
public class CoverageAnalyzer {

    private static final double BASE_COVERAGE = 50.0;
    private static final double FEATURE_WEIGHT = 5.0;
    private static final double RISK_DEDUCTION = 2.0;
    private static final double MAX_COVERAGE = 100.0;
    private static final double MIN_COVERAGE = 0.0;

    /**
     * Calculates the functional, business, and module coverage for the given requirement.
     *
     * @param requirement The parsed Requirement model
     * @return RequirementCoverage containing the calculated percentages
     */
    public RequirementCoverage calculateCoverage(Requirement requirement) {
        int featureCount = requirement.getFeatures() != null ? requirement.getFeatures().size() : 0;
        int riskCount = requirement.getRisks() != null ? requirement.getRisks().size() : 0;

        double functionalCoverage = calculateMetric(BASE_COVERAGE + (featureCount * FEATURE_WEIGHT));
        double businessCoverage = calculateMetric(BASE_COVERAGE + (featureCount * FEATURE_WEIGHT) - (riskCount * RISK_DEDUCTION));
        double overallCoverage = calculateMetric((functionalCoverage + businessCoverage) / 2.0);

        return new RequirementCoverage(functionalCoverage, businessCoverage, overallCoverage);
    }

    private double calculateMetric(double rawScore) {
        if (rawScore > MAX_COVERAGE) {
            return MAX_COVERAGE;
        }
        if (rawScore < MIN_COVERAGE) {
            return MIN_COVERAGE;
        }
        return rawScore;
    }
}