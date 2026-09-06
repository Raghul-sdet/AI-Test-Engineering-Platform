package com.enterprise.banking.ai.exploration.anomaly;

import com.enterprise.banking.ai.exploration.navigation.NavigationNode;
import java.util.List;

/**
 * Analyzes navigation history to identify cyclical UI behavior preventing exploration progress.
 */
public class InfiniteLoopDetector {

    public boolean detectLoop(List<NavigationNode> path) {
        if (path == null || path.size() < 4) return false;
        
        int size = path.size();
        NavigationNode last = path.get(size - 1);
        NavigationNode previous = path.get(size - 3); // A -> B -> A -> B pattern
        
        return last.equals(previous);
    }
}