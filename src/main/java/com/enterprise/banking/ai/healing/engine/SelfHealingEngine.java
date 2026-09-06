package com.enterprise.banking.ai.healing.engine;

import com.enterprise.banking.ai.healing.model.HealingCandidate;

import java.util.logging.Logger;

/**
 * Top-level logic controller for the self-healing process.
 */
public class SelfHealingEngine {

    private static final Logger LOGGER = Logger.getLogger(SelfHealingEngine.class.getName());
    private final LocatorHealingEngine locatorEngine;

    public SelfHealingEngine() {
        this.locatorEngine = new LocatorHealingEngine();
    }

    /**
     * Invokes the underlying locator engine to resolve a broken dependency.
     *
     * @param originalLocator Broken identifier
     * @param historicDomNode Stored DOM state
     * @return HealingCandidate object
     */
    public HealingCandidate resolveBrokenLocator(String originalLocator, String historicDomNode) {
        LOGGER.info("AI Self-Healing Engine activated for locator: " + originalLocator);
        return locatorEngine.generateCandidates(originalLocator, historicDomNode);
    }
}