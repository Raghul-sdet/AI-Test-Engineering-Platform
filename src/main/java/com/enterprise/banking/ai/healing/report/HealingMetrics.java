package com.enterprise.banking.ai.healing.report;

/**
 * Calculates and stores analytical metrics regarding the effectiveness of the AI healing engine.
 */
public class HealingMetrics {

    private double healingSuccessRate;
    private double locatorRecoveryPercentage;
    private double averageHealingTimeMillis;
    private int totalHealedElements;

    public HealingMetrics() {
    }

    public double getHealingSuccessRate() {
        return healingSuccessRate;
    }

    public void setHealingSuccessRate(double healingSuccessRate) {
        this.healingSuccessRate = healingSuccessRate;
    }

    public double getLocatorRecoveryPercentage() {
        return locatorRecoveryPercentage;
    }

    public void setLocatorRecoveryPercentage(double locatorRecoveryPercentage) {
        this.locatorRecoveryPercentage = locatorRecoveryPercentage;
    }

    public double getAverageHealingTimeMillis() {
        return averageHealingTimeMillis;
    }

    public void setAverageHealingTimeMillis(double averageHealingTimeMillis) {
        this.averageHealingTimeMillis = averageHealingTimeMillis;
    }

    public int getTotalHealedElements() {
        return totalHealedElements;
    }

    public void setTotalHealedElements(int totalHealedElements) {
        this.totalHealedElements = totalHealedElements;
    }
}