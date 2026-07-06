package com.enterprise.banking.ai.dom.model;

/**
 * Represents a potential locator for a DOM element.
 * Implements Comparable to allow sorting based on the strict ranking rules.
 */
public record LocatorCandidate(String strategy, String value, int rank) implements Comparable<LocatorCandidate> {
    
    @Override
    public int compareTo(LocatorCandidate other) {
        return Integer.compare(this.rank, other.rank);
    }
    
    @Override
    public String toString() {
        return strategy + "=" + value;
    }
}