package com.enterprise.banking.ai.execution.registry;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Turns a class's simple name (e.g. {@code "TransferFundsTest"}) into a set of lowercase
 * search keywords a generated scenario's text might plausibly contain (e.g. {@code "transfer"},
 * {@code "funds"}, {@code "transfer funds"}).
 * <p>
 * This is what lets {@link TestRegistry} and {@link PageObjectRegistry} auto-derive keyword
 * mappings from real class names instead of a hand-typed, single-domain (banking) keyword
 * list that had to be manually edited for every new class. The tradeoff versus the old
 * hand-curated list: this cannot invent domain synonyms that don't appear in the class name
 * itself (e.g. it won't know "payment" and "beneficiary" both mean TransferFundsTest unless
 * those words are literally in the name) - it trades some synonym richness for working
 * automatically on any class, in any domain, with zero maintenance.
 */
final class KeywordDeriver {

    private static final Pattern CAMEL_CASE_SPLIT = Pattern.compile("(?<=[a-z0-9])(?=[A-Z])");

    private KeywordDeriver() {
    }

    /**
     * @param simpleClassName e.g. "TransferFundsTest"
     * @param suffixToStrip   e.g. "Test" or "Page" - removed before splitting, if present
     * @return lowercase keywords: each individual CamelCase word, plus (if there's more than
     *         one word) the full space-joined phrase. Never null; empty if nothing usable
     *         remains after stripping the suffix.
     */
    static Set<String> deriveKeywords(String simpleClassName, String suffixToStrip) {
        Set<String> keywords = new LinkedHashSet<>();
        if (simpleClassName == null) {
            return keywords;
        }

        String base = simpleClassName;
        if (suffixToStrip != null && base.endsWith(suffixToStrip)) {
            base = base.substring(0, base.length() - suffixToStrip.length());
        }
        if (base.isEmpty()) {
            return keywords;
        }

        String[] words = CAMEL_CASE_SPLIT.split(base);
        StringBuilder phrase = new StringBuilder();
        for (String word : words) {
            String lower = word.toLowerCase();
            if (lower.isBlank()) {
                continue;
            }
            keywords.add(lower);
            if (phrase.length() > 0) {
                phrase.append(' ');
            }
            phrase.append(lower);
        }
        if (words.length > 1) {
            keywords.add(phrase.toString());
        }
        return keywords;
    }
}
