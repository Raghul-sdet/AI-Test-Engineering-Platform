package com.enterprise.banking.ai.mapper;

import com.enterprise.banking.ai.dom.model.DOMElement;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Provides semantic analysis to map natural language test steps to physical DOM elements.
 * Utilizes tokenization and synonym mapping rather than exact string comparison.
 */
public class SemanticMatcher {

    /**
     * Calculates a confidence score representing how well an Excel action matches a DOM element.
     *
     * @param actionStep The natural language step from Excel (e.g., "Enter Username").
     * @param element    The DOM element metadata.
     * @return A confidence score between 0.0 and 1.0.
     */
    public double calculateConfidence(String actionStep, DOMElement element) {
        String normalizedAction = normalize(actionStep);
        List<String> actionTokens = Arrays.asList(normalizedAction.split("\\s+"));
        
        String combinedElementMetadata = normalize(
            element.elementName() + " " + element.id() + " " + element.name() + " " + element.placeholder()
        );
        
        double matchCount = 0;
        for (String token : actionTokens) {
            if (isMeaningfulToken(token) && combinedElementMetadata.contains(token)) {
                matchCount++;
            }
        }
        
        // Boost score for semantic synonyms (e.g., login -> signin)
        if (actionTokens.contains("login") && combinedElementMetadata.contains("sign")) {
            matchCount += 1.5;
        }

        long meaningfulTokens = actionTokens.stream().filter(this::isMeaningfulToken).count();
        if (meaningfulTokens == 0) return 0.0;
        
        return Math.min(1.0, matchCount / meaningfulTokens);
    }

    private String normalize(String input) {
        if (input == null) return "";
        String normalized = input.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9\\s]", " ");
        // AI normalization to handle common spaced synonyms
        normalized = normalized.replace("log in", "login").replace("sign in", "signin");
        return normalized;
    }

    private boolean isMeaningfulToken(String token) {
        List<String> stopWords = Arrays.asList("enter", "click", "type", "the", "and", "valid", "invalid", "in", "on");
        return token.length() > 2 && !stopWords.contains(token);
    }
}