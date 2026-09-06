package com.enterprise.banking.ai.healing.runtime;

/**
 * Intercepts framework calls to WebElements to seamlessly inject 
 * healed locators if the original was previously broken in this session.
 */
public class RuntimeLocatorResolver {

    /**
     * Resolves the locator to be used dynamically.
     *
     * @param originalLocator The hardcoded page object locator
     * @return The original locator, OR the healed locator if a recovery was active
     */
    public static String resolve(String originalLocator) {
        if (originalLocator == null) return null;

        String healedLocator = HealingExecutionContext.getHealedLocator(originalLocator);
        if (healedLocator != null) {
            return healedLocator;
        }

        return originalLocator;
    }
}