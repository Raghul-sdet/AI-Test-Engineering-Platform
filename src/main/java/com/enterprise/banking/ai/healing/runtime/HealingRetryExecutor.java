package com.enterprise.banking.ai.healing.runtime;

import com.enterprise.banking.ai.exception.SelfHealingException;
import com.enterprise.banking.ai.healing.model.HealingResult;
import com.enterprise.banking.ai.healing.strategy.DomHealingStrategy;
import com.enterprise.banking.ai.healing.strategy.LocatorHealingStrategy;
import com.enterprise.banking.ai.healing.strategy.RetryHealingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper class to execute interactions securely. Captures specific exceptions
 * and delegates to the AI healing strategies dynamically.
 */
public class HealingRetryExecutor {

    private static final Logger LOGGER = Logger.getLogger(HealingRetryExecutor.class.getName());
    
    private final List<LocatorHealingStrategy> strategies;

    public HealingRetryExecutor(DomHealingStrategy domStrategy) {
        this.strategies = new ArrayList<>();
        this.strategies.add(new RetryHealingStrategy());
        this.strategies.add(domStrategy);
    }

    /**
     * Executes a runnable task. If it fails due to a recognized automation exception, 
     * intercepts the failure and attempts AI self-healing.
     *
     * @param action          The lambda/runnable interaction logic
     * @param originalLocator The locator targeted by the action
     * @param domContext      The historical DOM of the target
     * @return HealingResult details if healing occurred, or null if execution passed naturally
     */
    public HealingResult executeWithHealing(Runnable action, String originalLocator, String domContext) {
        long startTime = System.currentTimeMillis();
        
        try {
            // First attempt with original or previously healed locator
            action.run();
            return null; // No healing required
            
        } catch (RuntimeException ex) {
            String exceptionName = ex.getClass().getSimpleName();
            LOGGER.log(Level.WARNING, "Execution failed with {0}. Triggering AI Healing Engine.", exceptionName);
            
            for (LocatorHealingStrategy strategy : strategies) {
                if (strategy.canHeal(exceptionName)) {
                    HealingResult result = strategy.executeHealing(originalLocator, domContext);
                    result.setExecutionTimeMillis(System.currentTimeMillis() - startTime);
                    
                    if (result.isSuccessful()) {
                        LOGGER.info("Healing successful. Resuming execution.");
                        return result;
                    }
                }
            }
            LOGGER.severe("All healing strategies exhausted. Throwing original exception.");
            throw new SelfHealingException("Execution failed and could not be self-healed.", ex);
        }
    }
}