package com.enterprise.banking.tests;

import com.enterprise.banking.ai.exception.SelfHealingException;
import com.enterprise.banking.ai.healing.model.HealingAction;
import com.enterprise.banking.ai.healing.model.HealingResult;
import com.enterprise.banking.ai.healing.report.HealingReport;
import com.enterprise.banking.ai.healing.runtime.RuntimeLocatorResolver;
import com.enterprise.banking.ai.healing.service.SelfHealingService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

/**
 * Validates the core interception, generation, and tracking logic 
 * of the AI Self-Healing Engine.
 */
public class SelfHealingEngineTest {

    private SelfHealingService healingService;

    @BeforeMethod
    public void setUp() {
        healingService = new SelfHealingService();
        healingService.startExecutionBoundary("EXEC-TEST-001");
    }

    @Test
    public void testSuccessfulLocatorHealing() {
        String originalLocator = "//button[@id='submit-btn']";
        String historicDom = "<button id=\"submit-btn\" class=\"primary-btn\" data-testid=\"login-submit\">Login</button>";

        // Mock an execution block that throws NoSuchElement to trigger healing
        Runnable failingAction = () -> {
            // Check if context has been healed, if not throw exception
            String currentTarget = RuntimeLocatorResolver.resolve(originalLocator);
            if (currentTarget.equals(originalLocator)) {
                throw new NoSuchElementException("Unable to locate element: " + originalLocator);
            }
            // Second loop iteration simulates success after context is healed
        };

        // Act
        healingService.executeAction(failingAction, originalLocator, historicDom);

        // Assert Execution Context updated
        String activeLocator = RuntimeLocatorResolver.resolve(originalLocator);
        Assert.assertNotEquals(activeLocator, originalLocator, "Locator should be updated in runtime context.");
        Assert.assertTrue(activeLocator.contains("data-testid") || activeLocator.contains("submit-btn"), "Healed locator should derive from DOM.");

        // Assert Reporting Metrics
        HealingReport report = healingService.generateFinalReport();
        Assert.assertNotNull(report, "Healing report should be generated.");
        Assert.assertEquals(report.getDetailedInterventions().size(), 1, "One intervention should be logged.");
        
        HealingResult result = report.getDetailedInterventions().get(0);
        Assert.assertTrue(result.isSuccessful(), "Healing action should mark as successful.");
        Assert.assertEquals(result.getActionTaken(), HealingAction.LOCATOR_REPLACED, "Action should be locator replacement.");
        
        Assert.assertEquals(report.getMetrics().getHealingSuccessRate(), 100.0, "Success rate should be 100%.");
    }

    @Test(expectedExceptions = SelfHealingException.class)
    public void testHealingFailureThrowsException() {
        String originalLocator = "//div[@id='unknown']";
        String emptyDom = ""; // No data to generate fallbacks

        Runnable fatalAction = () -> {
            throw new NoSuchElementException("Element absolutely missing.");
        };

        // Since DOM is empty, engine cannot generate fallbacks, so it throws SelfHealingException
        healingService.executeAction(fatalAction, originalLocator, emptyDom);
    }
}