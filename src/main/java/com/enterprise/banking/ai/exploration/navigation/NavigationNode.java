package com.enterprise.banking.ai.exploration.navigation;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a unique UI state or Page discovered during exploration.
 */
public class NavigationNode {
    private final String nodeId;
    private final String url;
    private final String domHash;
    private final String pageTitle;

    public NavigationNode(String url, String domHash, String pageTitle) {
        this.nodeId = "NODE-" + UUID.randomUUID().toString().substring(0, 6);
        this.url = url;
        this.domHash = domHash;
        this.pageTitle = pageTitle;
    }

    public String getNodeId() { return nodeId; }
    public String getUrl() { return url; }
    public String getDomHash() { return domHash; }
    public String getPageTitle() { return pageTitle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NavigationNode that = (NavigationNode) o;
        return Objects.equals(domHash, that.domHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domHash);
    }
}