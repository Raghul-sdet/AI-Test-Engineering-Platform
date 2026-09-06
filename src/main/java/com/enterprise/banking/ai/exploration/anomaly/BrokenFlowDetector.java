package com.enterprise.banking.ai.exploration.anomaly;

/**
 * Detects HTTP 4xx/5xx or unhandled exception stack traces visible in the UI DOM.
 */
public class BrokenFlowDetector implements AnomalyDetector {

    @Override
    public boolean detect(String domSource) {
        if (domSource == null) return false;
        String lower = domSource.toLowerCase();
        return lower.contains("500 internal server error") || 
               lower.contains("404 not found") || 
               lower.contains("java.lang.nullpointerexception");
    }
}