package com.enterprise.banking.ai.notification;

import com.enterprise.banking.ai.notification.email.EmailConnector;
import com.enterprise.banking.ai.notification.slack.SlackConnector;
import com.enterprise.banking.ai.notification.teams.TeamsConnector;
import com.enterprise.banking.ai.notification.webhook.WebhookConnector;
import java.util.logging.Logger;

/**
 * The Master Controller Facade for Phase 7 Step 2: Notification & Collaboration Engine.
 * Serves as the central API for the framework to broadcast results globally.
 */
public class NotificationEngine {
    private static final Logger LOGGER = Logger.getLogger(NotificationEngine.class.getName());

    private final NotificationManager manager;
    private final NotificationDispatcher dispatcher;
    private final NotificationHistory history;

    public NotificationEngine() {
        this.manager = new NotificationManager();
        this.history = new NotificationHistory();
        this.dispatcher = new NotificationDispatcher(this.history);
    }

    /**
     * Bootstraps the enterprise communication channels with default configurations.
     */
    public void initializeStandardChannels() {
        LOGGER.info("Initializing Standard Enterprise Notification Channels...");
        
        NotificationConfiguration defaultConfig = new NotificationConfiguration();
        
        manager.registerChannel(new SlackConnector(), defaultConfig);
        manager.registerChannel(new TeamsConnector(), defaultConfig);
        manager.registerChannel(new EmailConnector(), defaultConfig);
        manager.registerChannel(new WebhookConnector(), defaultConfig);
    }

    /**
     * Broadcasts a formatted template to all registered channels.
     *
     * @param context Urgency and routing context
     * @param template Payload containing title, message, and severity
     */
    public void broadcast(NotificationContext context, NotificationTemplate template) {
        dispatcher.dispatchAsync(context, template);
        // Force process for architectural sync (Normally runs on a background thread pool)
        dispatcher.processQueue(manager.getActiveChannels());
    }

    /**
     * Retrieves the delivery outcome audit log.
     *
     * @return NotificationHistory object
     */
    public NotificationHistory getHistory() {
        return history;
    }

    public NotificationManager getManager() {
        return manager;
    }
}