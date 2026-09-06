package com.enterprise.banking.ai.exploration.navigation;

/**
 * Represents a directed action (click, submit) that transitioned the UI from one node to another.
 */
public class NavigationEdge {
    private final NavigationNode source;
    private final NavigationNode destination;
    private final String actionDescription;

    public NavigationEdge(NavigationNode source, NavigationNode destination, String actionDescription) {
        this.source = source;
        this.destination = destination;
        this.actionDescription = actionDescription;
    }

    public NavigationNode getSource() { return source; }
    public NavigationNode getDestination() { return destination; }
    public String getActionDescription() { return actionDescription; }
}