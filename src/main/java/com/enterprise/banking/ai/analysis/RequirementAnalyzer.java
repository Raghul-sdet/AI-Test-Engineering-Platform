package com.enterprise.banking.ai.analysis;

import com.enterprise.banking.ai.exception.RequirementAnalysisException;
import com.enterprise.banking.ai.model.Requirement;
import com.enterprise.banking.ai.model.RequirementFeature;
import com.enterprise.banking.ai.model.RequirementPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Engine responsible for reading raw text requirements, parsing, 
 * normalizing, and extracting core features and structures.
 */
public class RequirementAnalyzer {

    private static final Logger LOGGER = Logger.getLogger(RequirementAnalyzer.class.getName());
    private static final String PARAGRAPH_SPLIT_REGEX = "\\n{2,}";
    private static final String CARRIAGE_RETURN_REGEX = "\\r\\n";
    private static final String NEW_LINE = "\n";
    private static final String DEFAULT_TITLE = "Untitled Requirement";
    private static final String FEATURE_PREFIX = "FEAT-";
    private static final String DEFAULT_MODULE = "Core System";

    /**
     * Analyzes the raw requirement string and builds the foundational Requirement object.
     *
     * @param rawRequirement The raw text input of the business requirement
     * @return Structured Requirement object
     */
    public Requirement extractRequirementStructure(String rawRequirement) {
        if (rawRequirement == null || rawRequirement.trim().isEmpty()) {
            throw new RequirementAnalysisException("Raw requirement input cannot be null or empty.");
        }

        LOGGER.info("Starting requirement text normalization and extraction.");
        String normalizedText = normalizeText(rawRequirement);
        String[] paragraphs = normalizedText.split(PARAGRAPH_SPLIT_REGEX);

        Requirement requirement = new Requirement();
        requirement.setId(UUID.randomUUID().toString());
        requirement.setTitle(extractTitle(paragraphs));
        requirement.setDescription(normalizedText);
        requirement.setPriority(evaluatePriority(normalizedText));
        requirement.setFeatures(extractFeatures(paragraphs));

        return requirement;
    }

    private String normalizeText(String rawRequirement) {
        return rawRequirement.replaceAll(CARRIAGE_RETURN_REGEX, NEW_LINE).trim();
    }

    private String extractTitle(String[] paragraphs) {
        if (paragraphs.length > 0) {
            return paragraphs[0].lines().findFirst().orElse(DEFAULT_TITLE);
        }
        return DEFAULT_TITLE;
    }

    private List<RequirementFeature> extractFeatures(String[] paragraphs) {
        List<RequirementFeature> features = new ArrayList<>();
        int featureCounter = 1;

        for (String paragraph : paragraphs) {
            String lowerPara = paragraph.toLowerCase();
            if (lowerPara.contains("feature") || lowerPara.contains("module") || lowerPara.contains("user can")) {
                String featureName = paragraph.lines().findFirst().orElse("Identified Feature " + featureCounter);
                features.add(new RequirementFeature(
                        FEATURE_PREFIX + featureCounter,
                        featureName,
                        DEFAULT_MODULE,
                        paragraph
                ));
                featureCounter++;
            }
        }

        if (features.isEmpty()) {
            features.add(new RequirementFeature(
                    FEATURE_PREFIX + featureCounter,
                    "Baseline Functional Scope",
                    DEFAULT_MODULE,
                    "Extracted implicitly from raw description"
            ));
        }

        return features;
    }

    private RequirementPriority evaluatePriority(String text) {
        String lowerText = text.toLowerCase();
        if (lowerText.contains("critical") || lowerText.contains("urgent") || lowerText.contains("blocker")) {
            return RequirementPriority.CRITICAL;
        } else if (lowerText.contains("high") || lowerText.contains("priority 1")) {
            return RequirementPriority.HIGH;
        } else if (lowerText.contains("low") || lowerText.contains("nice to have")) {
            return RequirementPriority.LOW;
        }
        return RequirementPriority.MEDIUM;
    }
}