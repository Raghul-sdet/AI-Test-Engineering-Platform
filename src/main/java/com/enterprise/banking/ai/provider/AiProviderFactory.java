package com.enterprise.banking.ai.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.enterprise.banking.ai.exception.AiExtensionException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
/**
 * Factory for resolving the appropriate AI Provider based on environment variables.
 */
public class AiProviderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiProviderFactory.class);

    public static AiProvider createProvider() {
        if (isOllamaAvailable()) {
            LOGGER.info("Ollama is available. Using OllamaAiProvider.");
            return new OllamaAiProvider();
        }

        throw new AiExtensionException(
                "Ollama is not running. Install it from https://ollama.com, " +
                "run `ollama pull qwen2.5:3b-instruct`, then run `ollama serve`.");
    }

    private static boolean isOllamaAvailable() {
        try {
            String host = System.getProperty("ollama.host", "http://localhost:11434");
            String endpoint = host.endsWith("/") ? host + "api/tags" : host + "/api/tags";

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(2))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .GET()
                    .timeout(Duration.ofSeconds(2))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
