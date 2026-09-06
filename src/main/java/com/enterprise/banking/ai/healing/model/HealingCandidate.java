package com.enterprise.banking.ai.healing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the original broken locator and a prioritized list of fallback candidates.
 */
public class HealingCandidate {

    private String originalLocator;
    private List<LocatorCandidate> fallbackCandidates;

    public HealingCandidate(String originalLocator) {
        if (originalLocator == null || originalLocator.isEmpty()) {
            throw new IllegalArgumentException("Original locator cannot be null or empty.");
        }
        this.originalLocator = originalLocator;
        this.fallbackCandidates = new ArrayList<>();
    }

    public String getOriginalLocator() {
        return originalLocator;
    }

    public List<LocatorCandidate> getFallbackCandidates() {
        return new ArrayList<>(fallbackCandidates);
    }

    public void addCandidate(LocatorCandidate candidate) {
        if (candidate != null) {
            this.fallbackCandidates.add(candidate);
            Collections.sort(this.fallbackCandidates);
        }
    }
}