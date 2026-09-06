package com.enterprise.banking.ai.healing.engine;

import com.enterprise.banking.ai.healing.model.HealingCandidate;
import com.enterprise.banking.ai.healing.model.LocatorCandidate;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Core engine responsible for generating an array of potential fallback locators
 * when the original framework locator fails to find an element.
 */
public class LocatorHealingEngine {

    private static final Logger LOGGER = Logger.getLogger(LocatorHealingEngine.class.getName());
    private final DOMComparator domComparator;
    private final SimilarityEngine similarityEngine;

    public LocatorHealingEngine() {
        this.domComparator = new DOMComparator();
        this.similarityEngine = new SimilarityEngine();
    }

    /**
     * Synthesizes new locators by analyzing the historic DOM state of the broken element.
     *
     * @param originalLocator The locator that failed
     * @param historicDomNode The last known HTML string of the element
     * @return HealingCandidate containing prioritized fallbacks
     */
    public HealingCandidate generateCandidates(String originalLocator, String historicDomNode) {
        LOGGER.info("Generating fallback locators for broken locator: " + originalLocator);
        HealingCandidate candidateTracker = new HealingCandidate(originalLocator);
        
        Map<String, String> attributes = domComparator.extractAttributes(historicDomNode);
        String tag = historicDomNode != null && historicDomNode.contains(" ") 
                     ? historicDomNode.substring(1, historicDomNode.indexOf(" ")) 
                     : "input";

        // Priority 1: ID
        if (attributes.containsKey("id")) {
            String id = attributes.get("id");
            candidateTracker.addCandidate(new LocatorCandidate("CSS", CssSelectorGenerator.generateById(tag, id), 95.0));
            candidateTracker.addCandidate(new LocatorCandidate("XPATH", XPathGenerator.generateByAttribute(tag, "id", id), 90.0));
        }

        // Priority 2: Name
        if (attributes.containsKey("name")) {
            String name = attributes.get("name");
            candidateTracker.addCandidate(new LocatorCandidate("CSS", CssSelectorGenerator.generateByAttribute(tag, "name", name), 85.0));
            candidateTracker.addCandidate(new LocatorCandidate("XPATH", XPathGenerator.generateByAttribute(tag, "name", name), 80.0));
        }

        // Priority 3: Class & Data Attributes
        if (attributes.containsKey("class")) {
            String className = attributes.get("class");
            candidateTracker.addCandidate(new LocatorCandidate("CSS", CssSelectorGenerator.generateByClass(tag, className), 70.0));
        }
        
        if (attributes.containsKey("data-testid")) {
            String dataTestId = attributes.get("data-testid");
            candidateTracker.addCandidate(new LocatorCandidate("CSS", CssSelectorGenerator.generateByAttribute(tag, "data-testid", dataTestId), 99.0));
        }

        // Adjust confidence scores dynamically based on similarity to original
        adjustConfidenceScores(candidateTracker, originalLocator);

        return candidateTracker;
    }

    private void adjustConfidenceScores(HealingCandidate tracker, String originalLocator) {
        for (LocatorCandidate candidate : tracker.getFallbackCandidates()) {
            double simScore = similarityEngine.calculateSimilarityScore(originalLocator, candidate.getLocatorValue());
            // Boost score slightly if visually similar to original broken path
            if (simScore > 50.0) {
                candidate.setConfidenceScore(Math.min(100.0, candidate.getConfidenceScore() + 5.0));
            }
        }
    }
}