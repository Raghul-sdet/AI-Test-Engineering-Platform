package com.enterprise.banking.ai.provider;

import com.enterprise.banking.ai.exception.AiExtensionException;

/**
 * Core architectural contract for integrating with external AI language models (LLMs).
 * Employs the Strategy Pattern and Interface Segregation to support both modern 
 * Phase-1 orchestration pipelines and legacy analysis engines seamlessly.
 */
public interface AiProvider {

    /**
     * The foundational execution contract required by legacy implementations 
     * such as MockAiProvider, OpenAiProvider, and RequirementAnalyzer.
     *
     * @param systemPrompt The structural and behavioral instructions directing the AI model.
     * @param userPrompt   The dynamic user payload or context to be analyzed.
     * @return The raw, parsed text response from the AI Engine.
     * @throws AiExtensionException if the API transaction fails or returns an invalid payload.
     */
    String executeCompletion(String systemPrompt, String userPrompt) throws AiExtensionException;

    /**
     * Modern orchestration contract utilized by Phase-1 generators (e.g., Scenario, TestCase).
     * Acts as a default bridge routing to the legacy execution method, preventing the need 
     * to rewrite existing provider implementations while enabling new components to compile.
     *
     * @param systemPrompt The structural and behavioral instructions.
     * @param userPrompt   The dynamic user payload.
     * @return The raw text response retrieved via the legacy implementation.
     * @throws AiExtensionException if the underlying transaction fails.
     */
    default String generateResponse(String systemPrompt, String userPrompt) throws AiExtensionException {
        return executeCompletion(systemPrompt, userPrompt);
    }

    /**
     * Modern orchestration contract with an explicit temperature parameter for advanced AI tuning.
     * Default implementation acts as a fallback that delegates to the standard execution method.
     *
     * @param systemPrompt The structural and behavioral instructions.
     * @param userPrompt   The variable user payload.
     * @param temperature  The degree of determinism required for the generation.
     * @return The raw text response.
     * @throws AiExtensionException if the underlying transaction fails.
     */
    default String generateResponse(String systemPrompt, String userPrompt, double temperature) throws AiExtensionException {
        return executeCompletion(systemPrompt, userPrompt);
    }
}