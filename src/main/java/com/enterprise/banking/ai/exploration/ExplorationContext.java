package com.enterprise.banking.ai.exploration;

import java.time.LocalDateTime;

/**
 * Encapsulates the runtime context, boundaries, and objectives for an exploratory session.
 */
public class ExplorationContext {
    private final String startUrl;
    private final int maxDepth;
    private final int maxDurationMinutes;
    private final ExplorationStrategy strategy;
    private final LocalDateTime startTime;

    public ExplorationContext(String startUrl, int maxDepth, int maxDurationMinutes, ExplorationStrategy strategy) {
        this.startUrl = startUrl;
        this.maxDepth = maxDepth;
        this.maxDurationMinutes = maxDurationMinutes;
        this.strategy = strategy;
        this.startTime = LocalDateTime.now();
    }

    public String getStartUrl() { return startUrl; }
    public int getMaxDepth() { return maxDepth; }
    public int getMaxDurationMinutes() { return maxDurationMinutes; }
    public ExplorationStrategy getStrategy() { return strategy; }
    public LocalDateTime getStartTime() { return startTime; }
}