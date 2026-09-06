package com.enterprise.banking.ai.exploration.navigation;

/**
 * Manages active state tracking during traversal of the application under test.
 */
public class NavigationTracker {
    private final NavigationGraph graph;
    private final NavigationHistory history;
    private NavigationNode currentNode;

    public NavigationTracker() {
        this.graph = new NavigationGraph();
        this.history = new NavigationHistory();
    }

    public void updateCurrentState(NavigationNode newNode, String triggerAction) {
        graph.addNode(newNode);
        
        if (currentNode != null) {
            graph.addEdge(currentNode, newNode, triggerAction);
        }
        
        currentNode = newNode;
        history.recordVisit(newNode);
    }

    public NavigationNode getCurrentNode() { return currentNode; }
    public NavigationGraph getGraph() { return graph; }
    public NavigationHistory getHistory() { return history; }
}