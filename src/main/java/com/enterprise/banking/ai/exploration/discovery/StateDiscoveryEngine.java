package com.enterprise.banking.ai.exploration.discovery;

import com.enterprise.banking.ai.exploration.navigation.NavigationNode;

/**
 * Combines URL, Title, and Component analysis to evaluate if the UI has 
 * transitioned into a fundamentally new state requiring exploration.
 */
public class StateDiscoveryEngine {
    private final PageDiscoveryEngine pageDiscovery = new PageDiscoveryEngine();

    public NavigationNode identifyState(String currentUrl, String pageTitle, String domSource) {
        return pageDiscovery.discoverCurrentPage(currentUrl, pageTitle, domSource);
    }
}