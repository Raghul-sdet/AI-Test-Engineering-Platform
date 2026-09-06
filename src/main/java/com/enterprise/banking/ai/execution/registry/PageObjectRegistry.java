package com.enterprise.banking.ai.execution.registry;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Registry holding mappings between business keywords and existing Selenium Page Objects.
 * <p>
 * Previously hand-typed and banking-specific - see {@link TestRegistry}'s javadoc for the
 * same rationale. Now discovers page object classes automatically by scanning the configured
 * base package.
 */
public class PageObjectRegistry {

    private static final Logger LOGGER = Logger.getLogger(PageObjectRegistry.class.getName());
    private final Map<String, String> keywordToPageMap;
    private static final String DEFAULT_PAGE = "com.enterprise.banking.pages.BasePage";

    /**
     * Base package scanned for page object classes. Override with -Dpages.basePackage=...
     * to point this framework at a different project's page objects without touching
     * source code.
     */
    private static final String BASE_PACKAGE =
            System.getProperty("pages.basePackage", "com.enterprise.banking.pages");

    /**
     * Initializes the registry by scanning {@link #BASE_PACKAGE} for page object classes.
     */
    public PageObjectRegistry() {
        keywordToPageMap = new HashMap<>();
        initializeRegistry();
    }

    private void initializeRegistry() {
        for (Class<?> clazz : ClasspathClassScanner.scanPackage(BASE_PACKAGE)) {
            if (!isPageObjectClass(clazz)) {
                continue;
            }
            for (String keyword : KeywordDeriver.deriveKeywords(clazz.getSimpleName(), "Page")) {
                keywordToPageMap.putIfAbsent(keyword, clazz.getName());
            }
        }
        LOGGER.info("PageObjectRegistry initialized with " + keywordToPageMap.size()
                + " keyword mappings (auto-discovered from " + BASE_PACKAGE + ").");
    }

    private boolean isPageObjectClass(Class<?> clazz) {
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            return false;
        }
        String simpleName = clazz.getSimpleName();
        return simpleName.endsWith("Page") && !simpleName.equals("BasePage");
    }

    /**
     * Returns the fully qualified existing page object matching the keyword.
     *
     * @param keyword Business context keyword
     * @return Fully qualified page object class name, or the default base page if none matches
     */
    public String getPageObject(String keyword) {
        return keywordToPageMap.getOrDefault(keyword.toLowerCase(), DEFAULT_PAGE);
    }

    /**
     * Returns all registered keywords for analysis matching.
     *
     * @return Set of registered keywords
     */
    public Set<String> getRegisteredKeywords() {
        return keywordToPageMap.keySet();
    }
}
