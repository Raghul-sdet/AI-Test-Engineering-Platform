package com.enterprise.banking.ai.notification;

/**
 * Strategic interface for all external communication platforms.
 */
public interface NotificationChannel {
    
    /**
     * Initializes the channel connection using the provided configuration.
     * @param config Configuration map
     * @return true if initialized successfully
     */
    boolean initialize(NotificationConfiguration config);
    
    /**
     * Dispatches the template payload to the specific external API.
     * @param template Formatted message wrapper
     * @return true if delivery successful
     */
    boolean send(NotificationTemplate template);
    
    /**
     * Returns the formal name of the channel.
     * @return channel name
     */
    String getChannelName();
}