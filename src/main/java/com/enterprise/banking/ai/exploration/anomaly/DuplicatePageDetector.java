package com.enterprise.banking.ai.exploration.anomaly;

import com.enterprise.banking.ai.exploration.navigation.NavigationNode;
import java.util.Set;

/**
 * Identifies structurally identical pages loaded via different URLs or dynamic routes.
 */
public class DuplicatePageDetector {

    public boolean detect(NavigationNode newNode, Set<NavigationNode> existingNodes) {
        return existingNodes.stream().anyMatch(node -> node.getDomHash().equals(newNode.getDomHash()));
    }
}