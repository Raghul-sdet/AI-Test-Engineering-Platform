package com.enterprise.banking.ai.execution.registry;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Registry holding mappings between business keywords and existing TestNG test classes.
 * <p>
 * Previously this held a hand-typed, banking-specific map (e.g. "login" -&gt; LoginTest,
 * "transfer" -&gt; TransferFundsTest) that had to be manually edited every time a new site's
 * test classes were added - it would silently never learn about a class no one remembered to
 * register. It now discovers real test classes automatically by scanning the configured base
 * package at runtime and deriving keywords from each class's name (see {@link KeywordDeriver}).
 * Add a new *Test class that extends BaseTest for any site, and it's picked up with zero code
 * changes here.
 */
public class TestRegistry {

    private static final Logger LOGGER = Logger.getLogger(TestRegistry.class.getName());
    private final Map<String, String> keywordToTestMap;
    private static final String DEFAULT_TEST = "com.enterprise.banking.tests.BaseTest";

    /**
     * Base package scanned for test classes. Override with -Dtests.basePackage=... to point
     * this framework at a different project's test classes without touching source code.
     */
    private static final String BASE_PACKAGE =
            System.getProperty("tests.basePackage", "com.enterprise.banking.tests");

    /**
     * Initializes the registry by scanning {@link #BASE_PACKAGE} for business test classes.
     */
    public TestRegistry() {
        keywordToTestMap = new HashMap<>();
        initializeRegistry();
    }

    private void initializeRegistry() {
        for (Class<?> clazz : ClasspathClassScanner.scanPackage(BASE_PACKAGE)) {
            if (!isBusinessTestClass(clazz)) {
                continue;
            }
            for (String keyword : KeywordDeriver.deriveKeywords(clazz.getSimpleName(), "Test")) {
                keywordToTestMap.putIfAbsent(keyword, clazz.getName());
            }
        }
        LOGGER.info("TestRegistry initialized with " + keywordToTestMap.size()
                + " keyword mappings (auto-discovered from " + BASE_PACKAGE + ").");
    }

    /**
     * A "business test class" is a concrete class ending in "Test" whose direct superclass
     * is literally named BaseTest - i.e. a real Selenium UI test driving a page, not an
     * internal AI-engine unit test (those extend Object directly, not BaseTest). Checked by
     * simple name, not by importing BaseTest: src/main cannot depend on src/test at compile
     * time, but both are on the same classpath once this actually runs under `mvn test`.
     */
    private boolean isBusinessTestClass(Class<?> clazz) {
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            return false;
        }
        String simpleName = clazz.getSimpleName();
        if (!simpleName.endsWith("Test") || simpleName.equals("BaseTest")) {
            return false;
        }
        Class<?> superclass = clazz.getSuperclass();
        return superclass != null && "BaseTest".equals(superclass.getSimpleName());
    }

    /**
     * Returns the fully qualified existing test class matching the keyword.
     *
     * @param keyword Business context keyword
     * @return Fully qualified class name, or the default base test if none matches
     */
    public String getTestClass(String keyword) {
        return keywordToTestMap.getOrDefault(keyword.toLowerCase(), DEFAULT_TEST);
    }

    /**
     * Returns all registered keywords for analysis matching.
     *
     * @return Set of registered keywords
     */
    public Set<String> getRegisteredKeywords() {
        return keywordToTestMap.keySet();
    }
}
