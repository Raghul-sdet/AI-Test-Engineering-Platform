package com.enterprise.banking.ai.healing.strategy;

import com.enterprise.banking.ai.healing.engine.SelfHealingEngine;
import com.enterprise.banking.ai.healing.model.HealingAction;
import com.enterprise.banking.ai.healing.model.HealingCandidate;
import com.enterprise.banking.ai.healing.model.HealingResult;
import com.enterprise.banking.ai.healing.model.LocatorCandidate;
import com.enterprise.banking.ai.healing.runtime.HealingExecutionContext;

import java.util.logging.Logger;

/**
 * Strategy focused on recovering from NoSuchElementException by querying the AI
 * engine for alternative DOM traversal paths (CSS/XPath).
 */
public class DomHealingStrategy implements LocatorHealingStrategy {

    private static final Logger LOGGER = Logger.getLogger(DomHealingStrategy.class.getName());
    private final SelfHealingEngine engine;

    public DomHealingStrategy(SelfHealingEngine engine) {
        this.engine = engine;
    }

    @Override
    public boolean canHeal(String exceptionName) {
        return exceptionName.contains("NoSuchElementException") 
            || exceptionName.contains("InvalidSelectorException");
    }

    @Override
    public HealingResult executeHealing(String originalLocator, String domContext) {
        LOGGER.info("Applying DOM Healing Strategy for missing element: " + originalLocator);
        
        HealingResult result = new HealingResult();
        result.setOriginalLocator(originalLocator);
        
        HealingCandidate candidates = engine.resolveBrokenLocator(originalLocator, domContext);
        
        if (!candidates.getFallbackCandidates().isEmpty()) {
            LocatorCandidate bestMatch = candidates.getFallbackCandidates().get(0);
            
            result.setAppliedLocator(bestMatch);
            result.setActionTaken(HealingAction.LOCATOR_REPLACED);
            result.setSuccessful(true); // Assuming execution passes with this candidate
            
            // Register in context so rest of test uses healed locator
            HealingExecutionContext.registerHealedLocator(originalLocator, bestMatch.getLocatorValue());
            LOGGER.info("Successfully healed locator. New strategy: " + bestMatch.getLocatorValue());
        } else {
            result.setActionTaken(HealingAction.HEALING_FAILED);
            result.setSuccessful(false);
            LOGGER.warning("DOM Healing Strategy failed to generate valid candidates.");
        }
        
        return result;
    }
}