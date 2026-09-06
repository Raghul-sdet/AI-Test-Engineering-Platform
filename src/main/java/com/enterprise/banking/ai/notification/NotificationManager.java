package com.enterprise.banking.ai.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Manages the registration and lifecycle of configured Notification Channels.
 */
public class NotificationManager {
    private static final Logger LOGGER = Logger.getLogger(NotificationManager.class.getName());
    private final List<NotificationChannel> activeChannels = new CopyOnWriteArrayList<>();

    public void registerChannel(NotificationChannel channel, NotificationConfiguration config) {
        if (channel != null && channel.initialize(config)) {
            activeChannels.add(channel);
            LOGGER.info("Notification Channel Registered: " + channel.getChannelName());
        } else {
            LOGGER.severe("Failed to initialize Notification Channel.");
        }
    }

    public void unregisterChannel(String channelName) {
        activeChannels.removeIf(c -> c.getChannelName().equals(channelName));
        LOGGER.info("Notification Channel Unregistered: " + channelName);
    }

    public List<NotificationChannel> getActiveChannels() {
        return new ArrayList<>(activeChannels);
    }
}