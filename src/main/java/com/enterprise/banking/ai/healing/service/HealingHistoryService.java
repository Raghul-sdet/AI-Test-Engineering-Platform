package com.enterprise.banking.ai.healing.service;

import com.enterprise.banking.ai.healing.model.HealingResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory repository tracking all self-healing interventions across 
 * an entire automation suite execution.
 */
public class HealingHistoryService {

    private final List<HealingResult> history;

    public HealingHistoryService() {
        this.history = Collections.synchronizedList(new ArrayList<>());
    }

    public void recordResult(HealingResult result) {
        if (result != null) {
            history.add(result);
        }
    }

    public List<HealingResult> getAllResults() {
        return new ArrayList<>(history);
    }
    
    public void clear() {
        history.clear();
    }
}