package com.enterprise.banking.ai.validation;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Validates the syntax, compilation, and structural integrity of AI-generated source code
 * before allowing execution.
 */
public class GeneratedTestValidator {

    /**
     * Dynamically compiles the generated Java source files.
     *
     * @param sourceDirectory The root directory containing the generated source code.
     * @return true if compilation is successful, false otherwise.
     */
    public boolean validateCompilation(String sourceDirectory) {
        System.out.println("\n[VALIDATOR] Initiating dynamic compilation for generated sources in: " + sourceDirectory);
        
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("[VALIDATOR-ERROR] JavaCompiler not found. Ensure execution is running on a JDK, not a JRE.");
            return false;
        }

        try (Stream<Path> paths = Files.walk(Paths.get(sourceDirectory))) {
            String[] javaFiles = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".java"))
                    .toArray(String[]::new);

            if (javaFiles.length == 0) {
                System.out.println("[VALIDATOR] No generated Java files found to compile.");
                return false;
            }

            int compilationResult = compiler.run(null, null, null, javaFiles);
            
            if (compilationResult == 0) {
                System.out.println("[VALIDATOR] Compilation SUCCESS. Syntax and imports are valid.");
                return true;
            } else {
                System.err.println("[VALIDATOR-ERROR] Compilation FAILED. Syntax or import errors detected.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("[VALIDATOR-ERROR] Exception during validation: " + e.getMessage());
            return false;
        }
    }
}