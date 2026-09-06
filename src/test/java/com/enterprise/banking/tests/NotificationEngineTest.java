package com.enterprise.banking.tests;

import com.enterprise.banking.ai.notification.NotificationContext;
import com.enterprise.banking.ai.notification.NotificationEngine;
import com.enterprise.banking.ai.notification.NotificationPriority;
import com.enterprise.banking.ai.notification.report.ExecutionSummaryNotification;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates End-to-End Multicast Broadcasting logic via the NotificationEngine.
 */
public class NotificationEngineTest {

    @Test
    public void testMultichannelBroadcastAndHistory() {
        NotificationEngine engine = new NotificationEngine();
        engine.initializeStandardChannels();

        Assert.assertEquals(engine.getManager().getActiveChannels().size(), 4, "Engine must boot all 4 standard enterprise channels.");

        NotificationContext context = new NotificationContext("SUITE_COMPLETE", NotificationPriority.NORMAL);
        
        // Broadcast the generated template
        engine.broadcast(context, ExecutionSummaryNotification.create("Regression Suite v2", 95, 5));

        // Validate History
        // 4 Channels x 1 Message = 4 History Logs
        Assert.assertEquals(engine.getHistory().getHistory().size(), 4, "History must contain 1 entry for each dispatched channel.");
        
        Assert.assertTrue(engine.getHistory().getHistory().stream()
                .filter(r -> r != null)
                .allMatch(r -> r.isSuccessful()), 
                "All mock dispatches should resolve to successful state.");
    }
}