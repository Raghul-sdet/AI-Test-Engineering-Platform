package com.enterprise.banking.ai.notification.slack;

/**
 * Defines standard target channels within an enterprise Slack workspace.
 */
public enum SlackChannel {
    QA_ALERTS("#qa-alerts"),
    SECURITY_INCIDENTS("#security-incidents"),
    PERFORMANCE_OPS("#perf-ops"),
    EXECUTIVE_DASHBOARD("#executive-dashboard");

    private final String channelName;

    SlackChannel(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() { return channelName; }
}