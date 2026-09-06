package com.enterprise.banking.ai.exploration.anomaly;

/**
 * Identifies terminal UI states containing no interactable elements for traversal.
 */
public class DeadEndDetector implements AnomalyDetector {

    @Override
    public boolean detect(String discoveredElementsJson) {
        return discoveredElementsJson == null || discoveredElementsJson.trim().isEmpty() || discoveredElementsJson.equals("[]");
    }
}