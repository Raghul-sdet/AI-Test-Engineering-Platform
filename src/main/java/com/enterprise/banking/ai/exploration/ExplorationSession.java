package com.enterprise.banking.ai.exploration;

import java.util.UUID;

/**
 * Tracks the state and boundaries of an active exploratory testing execution.
 */
public class ExplorationSession {
    private final String sessionId;
    private final ExplorationContext context;
    private volatile ExplorationState state;

    public ExplorationSession(ExplorationContext context) {
        this.sessionId = "EXPL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.context = context;
        this.state = ExplorationState.INITIALIZED;
    }

    public String getSessionId() { return sessionId; }
    public ExplorationContext getContext() { return context; }
    public ExplorationState getState() { return state; }
    public void setState(ExplorationState state) { this.state = state; }
}