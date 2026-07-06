package com.enterprise.banking.ai.provider;

import com.enterprise.banking.ai.exception.AiExtensionException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Production implementation of AiProvider using native Java 17 HttpClient.
 * Eliminates external dependency issues and encapsulates connection properties.
 */
public class OpenAiProvider implements AiProvider {

    private final HttpClient httpClient;
    private final String apiKey;
    private final String endpoint;
    private final String model;

    public OpenAiProvider() {
        // Read configuration values from system environment variables or system properties
        this.apiKey = System.getProperty("ai.api.key", System.getenv("AI_API_KEY"));
        this.endpoint = System.getProperty("ai.api.endpoint", "https://api.openai.com/v1/chat/completions");
        this.model = System.getProperty("ai.model.name", "gpt-4o");

        if (this.apiKey == null || this.apiKey.isBlank()) {
            throw new AiExtensionException("Initialization failed: AI_API_KEY is not defined in system environment/properties.");
        }

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    @Override
    public String executeCompletion(String systemPrompt, String userPrompt) {
        try {
            // Escaping prompt inputs to form a clean JSON payload without complex library dependency overhead
            String escapedSystem = escapeJson(systemPrompt);
            String escapedUser = escapeJson(userPrompt);

            String payload = """
                {
                  "model": "%s",
                  "response_format": { "type": "json_object" },
                  "messages": [
                    { "role": "system", "content": "%s" },
                    { "role": "user", "content": "%s" }
                  ],
                  "temperature": 0.2
                }
                """.formatted(this.model, escapedSystem, escapedUser);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + this.apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .timeout(Duration.ofSeconds(45))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new AiExtensionException("LLM API call failed with status code: " + response.statusCode() + " - " + response.body());
            }

            return response.body();
        } catch (Exception e) {
            throw new AiExtensionException("Critical failure executing LLM completion request", e);
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}