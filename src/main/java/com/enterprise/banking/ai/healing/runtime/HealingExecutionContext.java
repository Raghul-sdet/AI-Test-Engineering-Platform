package com.enterprise.banking.ai.healing.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains thread-safe context of known good (healed) locators during a single test execution.
 * Prevents the engine from re-healing the same broken locator multiple times in one run.
 */
public class HealingExecutionContext {

    private static final ThreadLocal<Map<String, String>> HEALED_LOCATORS = ThreadLocal.withInitial(HashMap::new);
    private static final ThreadLocal<String> CURRENT_EXECUTION_ID = new ThreadLocal<>();

    public static void setExecutionId(String executionId) {
        CURRENT_EXECUTION_ID.set(executionId);
    }

    public static String getExecutionId() {
        return CURRENT_EXECUTION_ID.get();
    }

    public static void registerHealedLocator(String original, String healed) {
        HEALED_LOCATORS.get().put(original, healed);
    }

    public static String getHealedLocator(String original) {
        return HEALED_LOCATORS.get().get(original);
    }

    public static void clear() {
        HEALED_LOCATORS.get().clear();
        CURRENT_EXECUTION_ID.remove();
    }
}