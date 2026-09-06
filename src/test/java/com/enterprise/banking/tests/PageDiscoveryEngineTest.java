package com.enterprise.banking.tests;

import com.enterprise.banking.ai.exploration.discovery.PageDiscoveryEngine;
import com.enterprise.banking.ai.exploration.navigation.NavigationNode;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates DOM hashing and UI State Identification algorithms.
 */
public class PageDiscoveryEngineTest {

    @Test
    public void testPageNodeGeneration() {
        PageDiscoveryEngine engine = new PageDiscoveryEngine();
        
        NavigationNode node1 = engine.discoverCurrentPage("url1", "Title A", "<html><body>Data</body></html>");
        NavigationNode node2 = engine.discoverCurrentPage("url2", "Title B", "<html><body>Data</body></html>");
        
        Assert.assertEquals(node1.getDomHash(), node2.getDomHash(), "Identical DOM sources must generate matching state hashes regardless of URL.");
        Assert.assertNotEquals(node1.getNodeId(), node2.getNodeId(), "Nodes must retain unique logical Identifiers.");
    }
}