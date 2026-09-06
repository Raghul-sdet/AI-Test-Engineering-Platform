package com.enterprise.banking.ai.execution.registry;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lightweight, dependency-free classpath scanner. Given a package name, walks every
 * matching directory on the runtime classpath and loads every {@code .class} file found
 * there. This project has no scanning library (e.g. Reflections) as a dependency, so this
 * does the minimum needed: resolve the package's directory via the current classloader,
 * walk it, turn file paths back into fully-qualified class names, and {@code Class.forName}
 * each one.
 * <p>
 * Deliberately has NO compile-time reference to any class in {@code src/test} - Maven's
 * {@code src/main} cannot depend on {@code src/test} at compile time. Callers that need to
 * filter by superclass (see {@link TestRegistry}) do so via reflection at runtime, comparing
 * simple class names as strings - that works because by the time this actually runs (inside
 * {@code mvn test}), both {@code target/classes} and {@code target/test-classes} are on the
 * same runtime classpath, even though they were compiled as two separate, one-directional
 * modules.
 */
final class ClasspathClassScanner {

    private static final Logger LOGGER = Logger.getLogger(ClasspathClassScanner.class.getName());

    private ClasspathClassScanner() {
    }

    /**
     * @param packageName dot-separated package to scan, e.g. "com.enterprise.banking.tests"
     * @return every top-level (non-nested) class found directly under that package on the
     *         current runtime classpath. Never null; returns an empty list if the package
     *         doesn't resolve to any directory (e.g. running from a packaged jar instead of
     *         loose class files, which this framework does not do during {@code mvn test}).
     */
    static List<Class<?>> scanPackage(String packageName) {
        List<Class<?>> found = new ArrayList<>();
        if (packageName == null || packageName.isBlank()) {
            return found;
        }
        String path = packageName.replace('.', '/');
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (!"file".equals(resource.getProtocol())) {
                    // Skip jar:/other protocols. Loose .class directories under target/ are
                    // what mvn test actually produces; a packaged jar would need a different
                    // (jar-entry-walking) strategy, not needed for this framework's build.
                    continue;
                }
                File directory = new File(resource.toURI());
                if (directory.isDirectory()) {
                    collectClasses(directory, packageName, found);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to scan package: " + packageName, e);
        }
        return found;
    }

    private static void collectClasses(File directory, String packageName, List<Class<?>> found) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        java.util.Arrays.sort(files);
        for (File file : files) {
            if (file.isDirectory()) {
                collectClasses(file, packageName + "." + file.getName(), found);
            } else if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - ".class".length());
                try {
                    found.add(Class.forName(className, false, Thread.currentThread().getContextClassLoader()));
                } catch (Throwable t) {
                    LOGGER.log(Level.FINE, "Skipping unloadable class: " + className, t);
                }
            }
        }
    }
}
