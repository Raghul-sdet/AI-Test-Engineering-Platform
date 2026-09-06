package com.enterprise.banking.ai.healing.model;

/**
 * Represents an AI-generated fallback locator with an associated confidence score.
 */
public class LocatorCandidate implements Comparable<LocatorCandidate> {

    private String strategyType;
    private String locatorValue;
    private double confidenceScore;

    public LocatorCandidate() {
    }

    public LocatorCandidate(String strategyType, String locatorValue, double confidenceScore) {
        this.strategyType = strategyType;
        this.locatorValue = locatorValue;
        this.confidenceScore = confidenceScore;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    public String getLocatorValue() {
        return locatorValue;
    }

    public void setLocatorValue(String locatorValue) {
        this.locatorValue = locatorValue;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    @Override
    public int compareTo(LocatorCandidate other) {
        // Sort descending by confidence score
        return Double.compare(other.confidenceScore, this.confidenceScore);
    }
}