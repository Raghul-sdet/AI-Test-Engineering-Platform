package com.enterprise.banking.ai.notification.teams;

/**
 * DTO representing an outgoing Teams webhook payload.
 */
public class TeamsMessage {
    private final String payloadJson;

    public TeamsMessage(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    public String getPayloadJson() { return payloadJson; }
}