package com.enterprise.banking.ai.healing.strategy;

import com.enterprise.banking.ai.healing.model.HealingResult;

/**
 * Interface defining rules for different types of self-healing strategies.
 */
public interface LocatorHealingStrategy {

    /**
     * Determines if this strategy can attempt to heal the given exception type.
     *
     * @param exceptionName The class name of the thrown exception
     * @return true if supported, false otherwise
     */
    boolean canHeal(String exceptionName);

    /**
     * Executes the specific healing logic.
     *
     * @param originalLocator The broken locator
     * @param domContext      The DOM context
     * @return HealingResult representing success or failure
     */
    HealingResult executeHealing(String originalLocator, String domContext);
}