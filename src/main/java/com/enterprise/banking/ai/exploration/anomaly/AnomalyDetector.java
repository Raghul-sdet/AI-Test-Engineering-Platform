package com.enterprise.banking.ai.exploration.anomaly;

/**
 * Core interface for heuristic and AI-driven anomaly detection engines.
 */
public interface AnomalyDetector {
    
    /**
     * Evaluates context to determine if an abnormal state exists.
     *
     * @param context Information about the current state transition
     * @return True if anomaly detected, false otherwise
     */
    boolean detect(String context);
}