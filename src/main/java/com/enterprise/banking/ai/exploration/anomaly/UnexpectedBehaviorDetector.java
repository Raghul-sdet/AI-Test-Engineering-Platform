package com.enterprise.banking.ai.exploration.anomaly;

/**
 * Detects UI behavior that drastically shifts structure without a logical context progression.
 */
public class UnexpectedBehaviorDetector implements AnomalyDetector {

    @Override
    public boolean detect(String stateTransitionContext) {
        return stateTransitionContext != null && stateTransitionContext.contains("UNEXPECTED_SHIFT");
    }
}