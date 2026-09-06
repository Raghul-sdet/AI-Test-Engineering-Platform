package com.enterprise.banking.ai.exploration.learning;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared knowledge graph utilized by the LearningEngine to influence future decision making.
 */
public class ExplorationKnowledgeBase {
    private final ConcurrentHashMap<String, Integer> actionSuccessRates = new ConcurrentHashMap<>();

    public void recordSuccess(String actionType) {
        actionSuccessRates.put(actionType, actionSuccessRates.getOrDefault(actionType, 0) + 1);
    }

    public void recordFailure(String actionType) {
        actionSuccessRates.put(actionType, actionSuccessRates.getOrDefault(actionType, 0) - 1);
    }
}