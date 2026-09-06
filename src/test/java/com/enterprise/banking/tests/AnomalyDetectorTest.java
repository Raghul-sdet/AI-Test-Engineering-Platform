package com.enterprise.banking.tests;

import com.enterprise.banking.ai.exploration.anomaly.BrokenFlowDetector;
import com.enterprise.banking.ai.exploration.anomaly.InfiniteLoopDetector;
import com.enterprise.banking.ai.exploration.navigation.NavigationNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Validates critical defensive checks during exploration.
 */
public class AnomalyDetectorTest {

    @Test
    public void testBrokenFlowDetection() {
        BrokenFlowDetector detector = new BrokenFlowDetector();
        
        Assert.assertTrue(detector.detect("<html><head><title>500 Internal Server Error</title></head></html>"), "Must detect HTTP 500 errors injected into DOM.");
        Assert.assertFalse(detector.detect("<html><body>Welcome</body></html>"), "Valid DOM must not trigger anomaly.");
    }
    
    @Test
    public void testInfiniteLoopDetection() {
        InfiniteLoopDetector detector = new InfiniteLoopDetector();
        
        NavigationNode nodeA = new NavigationNode("A", "hashA", "A");
        NavigationNode nodeB = new NavigationNode("B", "hashB", "B");
        
        // Simulating sequence: A -> B -> A -> B
        List<NavigationNode> path = Arrays.asList(nodeA, nodeB, nodeA, nodeB);
        
        Assert.assertTrue(detector.detectLoop(path), "Engine must mathematically identify ABAB cyclical loop patterns in navigation history.");
    }
}