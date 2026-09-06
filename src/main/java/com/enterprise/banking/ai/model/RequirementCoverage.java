package com.enterprise.banking.ai.model;

/**
 * Represents the calculated coverage metrics for a given requirement.
 */
public class RequirementCoverage {

    private double functionalCoverage;
    private double businessCoverage;
    private double overallCoverage;

    /**
     * Default constructor.
     */
    public RequirementCoverage() {
    }

    /**
     * Parameterized constructor.
     *
     * @param functionalCoverage Functional coverage percentage
     * @param businessCoverage   Business logic coverage percentage
     * @param overallCoverage    Overall aggregated coverage percentage
     */
    public RequirementCoverage(double functionalCoverage, double businessCoverage, double overallCoverage) {
        this.functionalCoverage = functionalCoverage;
        this.businessCoverage = businessCoverage;
        this.overallCoverage = overallCoverage;
    }

    public double getFunctionalCoverage() {
        return functionalCoverage;
    }

    public void setFunctionalCoverage(double functionalCoverage) {
        this.functionalCoverage = functionalCoverage;
    }

    public double getBusinessCoverage() {
        return businessCoverage;
    }

    public void setBusinessCoverage(double businessCoverage) {
        this.businessCoverage = businessCoverage;
    }

    public double getOverallCoverage() {
        return overallCoverage;
    }

    public void setOverallCoverage(double overallCoverage) {
        this.overallCoverage = overallCoverage;
    }
}