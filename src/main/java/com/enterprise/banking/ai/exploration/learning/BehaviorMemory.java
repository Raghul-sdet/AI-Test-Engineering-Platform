package com.enterprise.banking.ai.exploration.learning;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores historical insights learned about the application's unique behavior traits.
 */
public class BehaviorMemory {
    private final List<String> insights = new ArrayList<>();

    public void addInsight(String insight) {
        if (insight != null) insights.add(insight);
    }

    public List<String> getInsights() {
        return new ArrayList<>(insights);
    }
}