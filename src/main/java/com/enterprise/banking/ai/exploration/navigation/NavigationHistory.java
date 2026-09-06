package com.enterprise.banking.ai.exploration.navigation;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the chronological sequence of nodes visited during a specific session to prevent looping.
 */
public class NavigationHistory {
    private final List<NavigationNode> visitedPath = new ArrayList<>();

    public void recordVisit(NavigationNode node) {
        if (node != null) visitedPath.add(node);
    }

    public boolean hasVisited(NavigationNode node) {
        return visitedPath.contains(node);
    }

    public List<NavigationNode> getVisitedPath() {
        return new ArrayList<>(visitedPath);
    }
}