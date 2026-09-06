package com.enterprise.banking.ai.execution.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds raw execution data and timings collected directly from the TestNG runtime engine.
 * Thread-safe for parallel execution collection.
 */
public class RuntimeStatistics {

    private int passedCount;
    private int failedCount;
    private int skippedCount;
    private long totalDurationMillis;
    private final List<Long> executionTimings;

    /**
     * Default constructor initializing thread-safe collections.
     */
    public RuntimeStatistics() {
        this.executionTimings = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized void incrementPassed() {
        this.passedCount++;
    }

    public synchronized void incrementFailed() {
        this.failedCount++;
    }

    public synchronized void incrementSkipped() {
        this.skippedCount++;
    }

    public synchronized void addTiming(long timingMillis) {
        this.executionTimings.add(timingMillis);
        this.totalDurationMillis += timingMillis;
    }

    public int getPassedCount() {
        return passedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public long getTotalDurationMillis() {
        return totalDurationMillis;
    }

    public List<Long> getExecutionTimings() {
        return new ArrayList<>(executionTimings);
    }
}