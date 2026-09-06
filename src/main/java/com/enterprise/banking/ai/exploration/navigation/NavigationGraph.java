package com.enterprise.banking.ai.exploration.navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Directed Graph mapping the structural pathways discovered by the exploration engine.
 */
public class NavigationGraph {
    private final Set<NavigationNode> nodes = ConcurrentHashMap.newKeySet();
    private final List<NavigationEdge> edges = new ArrayList<>();

    public void addNode(NavigationNode node) {
        if (node != null) nodes.add(node);
    }

    public void addEdge(NavigationNode source, NavigationNode destination, String action) {
        if (source != null && destination != null) {
            edges.add(new NavigationEdge(source, destination, action));
        }
    }

    public Set<NavigationNode> getNodes() { return new HashSet<>(nodes); }
    public List<NavigationEdge> getEdges() { return new ArrayList<>(edges); }
}