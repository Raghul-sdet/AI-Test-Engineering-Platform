package com.enterprise.banking.ai.healing.strategy;

import com.enterprise.banking.ai.healing.model.HealingAction;
import com.enterprise.banking.ai.healing.model.HealingResult;

import java.util.logging.Logger;

/**
 * Strategy focused on healing elements based strictly on their visual or 
 * accessibility attributes (e.g., text, placeholder, aria-label) when IDs fail.
 */
public class AttributeHealingStrategy implements LocatorHealingStrategy {

    private static final Logger LOGGER = Logger.getLogger(AttributeHealingStrategy.class.getName());

    @Override
    public boolean canHeal(String exceptionName) {
        return exceptionName.contains("NoSuchElementException");
    }

    @Override
    public HealingResult executeHealing(String originalLocator, String domContext) {
        LOGGER.info("Evaluating Attribute Healing Strategy fallback.");
        HealingResult result = new HealingResult();
        result.setOriginalLocator(originalLocator);
        result.setActionTaken(HealingAction.HEALING_FAILED);
        result.setSuccessful(false);
        // Deferred to DomHealingStrategy primarily in this architecture layer
        return result;
    }
}