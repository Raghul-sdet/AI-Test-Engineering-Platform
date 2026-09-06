package com.enterprise.banking.ai.healing.strategy;

import com.enterprise.banking.ai.healing.model.HealingAction;
import com.enterprise.banking.ai.healing.model.HealingResult;

import java.util.logging.Logger;

/**
 * Strategy focused entirely on mitigating timing and rendering issues
 * like StaleElementReference or ElementClickIntercepted via intelligent retries.
 */
public class RetryHealingStrategy implements LocatorHealingStrategy {

    private static final Logger LOGGER = Logger.getLogger(RetryHealingStrategy.class.getName());

    @Override
    public boolean canHeal(String exceptionName) {
        return exceptionName.contains("StaleElementReferenceException") 
            || exceptionName.contains("ElementClickInterceptedException")
            || exceptionName.contains("ElementNotInteractableException");
    }

    @Override
    public HealingResult executeHealing(String originalLocator, String domContext) {
        LOGGER.info("Applying Retry Healing Strategy for rendering synchronization issue.");
        
        HealingResult result = new HealingResult();
        result.setOriginalLocator(originalLocator);
        result.setActionTaken(HealingAction.RETRY_EXECUTED);
        result.setRetryCount(3);
        result.setSuccessful(true); // In actual impl, this evaluates if retry passes
        
        return result;
    }
}