package com.enterprise.banking.ai.provider;

import com.enterprise.banking.ai.exception.AiExtensionException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OllamaAiProvider implements AiProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(OllamaAiProvider.class);

    private final HttpClient httpClient;
    private final String endpoint;
    private final String model;
    private final int timeoutSeconds;

    public OllamaAiProvider() {
        String host = System.getProperty("ollama.host", "http://localhost:11434");
        this.endpoint = host.endsWith("/") ? host + "api/generate" : host + "/api/generate";
        
        String propModel = System.getProperty("ollama.model.name");
        String envModel = System.getenv("OLLAMA_MODEL");
        this.model = propModel != null ? propModel : (envModel != null ? envModel : "qwen2.5:3b-instruct");
        
        this.timeoutSeconds = Integer.parseInt(System.getProperty("ollama.timeout.seconds", "120"));

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    @Override
    public String executeCompletion(String systemPrompt, String userPrompt) {
        int maxRetries = 2; // For connection refused
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String escapedSystem = escapeJson(systemPrompt);
                String escapedUser = escapeJson(userPrompt);
                
                String combinedPrompt = escapedSystem + "\\n\\n" + escapedUser;

                String payload = """
                    {
                      "model": "%s",
                      "prompt": "%s",
                      "stream": false
                    }
                    """.formatted(this.model, combinedPrompt);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(this.endpoint))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload))
                        .timeout(Duration.ofSeconds(this.timeoutSeconds))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new AiExtensionException("Ollama API call failed with status code: " + response.statusCode() + " - " + response.body());
                }

                return extractTextFromOllamaResponse(response.body());
            } catch (AiExtensionException e) {
                throw e; // Bubble up explicit API errors
            } catch (java.net.ConnectException e) {
                LOGGER.warn("Connection refused when trying to reach Ollama at {}. Attempt {} of {}.", this.endpoint, attempt, maxRetries);
                if (attempt == maxRetries) {
                    throw new AiExtensionException("Failed to connect to Ollama. Please verify that 'ollama serve' is running locally.", e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new AiExtensionException("Ollama retry interrupted during backoff", ie);
                }
            } catch (Exception e) {
                throw new AiExtensionException("Critical failure executing Ollama completion request", e);
            }
        }
        throw new AiExtensionException("Exhausted retries for Ollama API completion");
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

    private String extractTextFromOllamaResponse(String json) {
        String search = "\"response\":\"";
        int start = json.indexOf(search);
        if (start == -1) {
            search = "\"response\": \"";
            start = json.indexOf(search);
        }
        if (start == -1) return json; // fallback just in case
        start += search.length();

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\') {
                i++;
                if (i >= json.length()) break;
                char next = json.charAt(i);
                if (next == 'n') sb.append('\n');
                else if (next == 'r') sb.append('\r');
                else if (next == 't') sb.append('\t');
                else if (next == '"') sb.append('"');
                else if (next == '\\') sb.append('\\');
                else sb.append(next);
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
