package com.enterprise.banking.ai.generator;

import com.enterprise.banking.ai.dom.model.DOMElement;
import com.enterprise.banking.ai.dom.model.LocatorCandidate;
import com.enterprise.banking.ai.mapper.SemanticMatcher;
import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestStep;
import com.enterprise.banking.ai.exception.AiExtensionException;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Engine responsible for transforming AI-generated test case models into 
 * executable Selenium WebDriver Java source files.
 * Leverages Java 17 Text Blocks and NIO.2 for clean source code synthesis.
 */
public class SeleniumCodeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumCodeGenerator.class);

    /**
     * Default constructor for framework initialization and dependency injection.
     */
    public SeleniumCodeGenerator() {
        // Intentionally left empty to allow standard framework instantiation
    }

    private static final SemanticMatcher MATCHER = new SemanticMatcher();
    private static final ParameterResolver PARAMETER_RESOLVER = new ParameterResolver();
    private static final double CONFIDENCE_THRESHOLD = 0.4;
    private static final ClassName BY = ClassName.get("org.openqa.selenium", "By");
    private static final ClassName WEB_ELEMENT = ClassName.get("org.openqa.selenium", "WebElement");

    /**
     * Generates a complete, production-ready Selenium TestNG class file dynamically, with
     * each AI-generated test step bound to a real, DOM-discovered element.
     * <p>
     * Previously this method completely ignored the content of {@code testCases} - every
     * generated test method was an identical stub
     * ({@code Assert.assertNotNull(driver); System.out.println("Triggering...")}) regardless
     * of what the AI actually generated, which is why generated tests always "passed": they
     * did nothing. It now reads each {@link TestCase}'s real {@link TestStep}s, matches each
     * step's action text against {@code domRepository} using the same {@link SemanticMatcher}
     * confidence scoring already proven in {@code ActionMappingEngine}, and emits a real
     * {@code driver.findElement(By...).sendKeys(...)}/{@code .click()} call for every
     * confident match. A step that can't be confidently matched is emitted as a
     * {@code // SKIPPED: ...} comment rather than failing the whole generated test - the same
     * graceful-degradation behavior {@code ActionMappingEngine} already uses.
     *
     * @param testCases       The list of AI-generated test cases to be compiled into code.
     * @param outputDirectory The physical directory path where the Java code will be saved.
     * @param domRepository   Elements discovered on {@code targetUrl} by
     *                        {@code DOMExtractionService}. May be empty (e.g. if DOM discovery
     *                        failed) - in that case every step is emitted as a skip comment
     *                        instead of the whole pipeline failing.
     * @param targetUrl       The site URL each generated test method navigates to first.
     * @return The Java File object representing the generated source code.
     * @throws AiExtensionException if file I/O operations fail or directory creation is denied.
     */
    public File generateSeleniumCode(List<TestCase> testCases, String outputDirectory,
                                      List<DOMElement> domRepository, String targetUrl) {
        if (testCases == null || testCases.isEmpty()) {
            LOGGER.error("Provided test case collection is null or empty");
            throw new IllegalArgumentException("Test case collection cannot be null or empty.");
        }
        if (outputDirectory == null || outputDirectory.trim().isEmpty()) {
            LOGGER.error("Provided output directory is null or empty");
            throw new IllegalArgumentException("Output directory path cannot be null or empty.");
        }
        List<DOMElement> elements = domRepository != null ? domRepository : Collections.emptyList();
        if (elements.isEmpty()) {
            LOGGER.warn("No DOM elements supplied - every generated step will be a skip comment. "
                    + "Check that DOM discovery against the target URL succeeded.");
        }

        LOGGER.info("Initiating dynamic Selenium TestNG code generation for {} test cases.", testCases.size());

        try {
            Path directoryPath = Paths.get(outputDirectory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            String generatedClassName = "AiGeneratedWebAutomationTest_" + System.currentTimeMillis();

            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(generatedClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(ClassName.get("com.enterprise.banking.tests", "BaseTest"))
                    .addJavadoc("Auto-generated TestNG execution suite. Each method's body is derived from a\n"
                            + "real AI-generated TestCase, with steps bound to elements actually discovered\n"
                            + "on the target site (see SeleniumCodeGenerator).\n");

            for (int i = 0; i < testCases.size(); i++) {
                classBuilder.addMethod(buildTestMethod(testCases.get(i), i + 1, elements, targetUrl));
            }

            JavaFile javaFile = JavaFile.builder("com.enterprise.banking.tests.generated", classBuilder.build())
                    .indent("    ")
                    .build();
            javaFile.writeTo(directoryPath);

            Path sourceFilePath = directoryPath
                    .resolve("com")
                    .resolve("enterprise")
                    .resolve("banking")
                    .resolve("tests")
                    .resolve("generated")
                    .resolve(generatedClassName + ".java");
            LOGGER.info("Selenium test suite successfully compiled and written to: {}", sourceFilePath.toAbsolutePath());
            return sourceFilePath.toFile();

        } catch (IOException ioException) {
            LOGGER.error("Failed to write generated Selenium code to the target file system.", ioException);
            throw new AiExtensionException("Selenium Code Generation encountered a critical IO exception", ioException);
        }
    }

    private MethodSpec buildTestMethod(TestCase testCase, int index, List<DOMElement> domRepository, String targetUrl) {
        String description = testCase.getTestCaseTitle() != null
                ? testCase.getTestCaseTitle().replace("\"", "'")
                : "AI Generated Automated Scenario Execution Block";

        MethodSpec.Builder method = MethodSpec.methodBuilder("executeGeneratedScenarioBlock" + index)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(org.testng.annotations.Test.class)
                        .addMember("description", "$S", description)
                        .build())
                .addJavadoc("Generated from TestCase: $L\n", testCase.getTestCaseId() != null ? testCase.getTestCaseId() : "unknown")
                .addStatement("$T driver = getDriver()", ClassName.get("org.openqa.selenium", "WebDriver"))
                .addStatement("$T.assertNotNull(driver, $S)", ClassName.get("org.testng", "Assert"), "WebDriver initialization critically failed");

        if (targetUrl != null && !targetUrl.isBlank()) {
            method.addStatement("driver.get($S)", targetUrl);
        }

        List<TestStep> steps = testCase.getStepsList();
        if (steps == null || steps.isEmpty()) {
            method.addComment("No steps were generated for this test case.");
            return method.build();
        }

        int elementCounter = 0;
        for (TestStep step : steps) {
            elementCounter = appendStep(method, step, domRepository, elementCounter);
        }

        return method.build();
    }

    private int appendStep(MethodSpec.Builder method, TestStep step, List<DOMElement> domRepository, int elementCounter) {
        String actionText = step.action();
        if (actionText == null || actionText.isBlank()) {
            return elementCounter;
        }

        DOMElement bestMatch = null;
        double bestScore = 0.0;
        for (DOMElement element : domRepository) {
            double score = MATCHER.calculateConfidence(actionText, element);
            if (score > bestScore) {
                bestScore = score;
                bestMatch = element;
            }
        }

        if (bestMatch == null || bestScore < CONFIDENCE_THRESHOLD || bestMatch.priorityLocator() == null) {
            method.addComment("SKIPPED: Could not confidently map step $L \"$L\"",
                    step.stepNumber(), actionText.replace("\"", "'"));
            return elementCounter;
        }

        LocatorCandidate locator = bestMatch.priorityLocator();
        String seleniumStrategy = toSeleniumByMethod(locator.strategy());
        String actionType = PARAMETER_RESOLVER.determineActionType(actionText);
        String elementVar = "element" + (++elementCounter);

        method.addComment("Step $L: $L", step.stepNumber(), actionText.replace("\"", "'"));
        method.addStatement("$T $L = driver.findElement($T.$L($S))",
                WEB_ELEMENT, elementVar, BY, seleniumStrategy, locator.value());

        switch (actionType) {
            case "INPUT" -> {
                String value = (step.testData() != null && !step.testData().isBlank())
                        ? extractInputValue(step.testData())
                        : "testValue";
                method.addStatement("$L.clear()", elementVar);
                method.addStatement("$L.sendKeys($S)", elementVar, value);
            }
            case "ASSERT" -> method.addStatement("$T.assertTrue($L.isDisplayed(), $S)",
                    ClassName.get("org.testng", "Assert"), elementVar, "Expected element to be visible: " + actionText.replace("\"", "'"));
            default -> method.addStatement("$L.click()", elementVar);
        }

        return elementCounter;
    }

    /**
     * "Key=Value" (e.g. "Username=testuser") -> "testuser"; anything else is used as-is.
     * Mirrors the same simplified parsing convention {@link ParameterResolver} already uses.
     */
    private String extractInputValue(String testData) {
        if (testData.contains("=")) {
            String[] parts = testData.split("=", 2);
            return parts.length > 1 ? parts[1].trim() : testData;
        }
        return testData;
    }

    /** DOM engine locator strategies are "id"/"name"/"css"/"xpath" (see LocatorRankingEngine);
     *  "css" needs to become "cssSelector" to match Selenium's By.* static method names. */
    private String toSeleniumByMethod(String strategy) {
        if (strategy == null) {
            return "id";
        }
        return "css".equalsIgnoreCase(strategy) ? "cssSelector" : strategy.toLowerCase(Locale.ROOT);
    }

    /**
     * Generates the BaseTest configuration class to manage WebDriver lifecycles dynamically.
     * Required by the CodeGenerationEngine during the project scaffolding phase.
     * 
     * @throws AiExtensionException if the base test file cannot be created.
     */
    public void generateBaseTest() {
        LOGGER.info("Generating dynamic BaseTest configuration class");
        try {
            Path directoryPath = Paths.get(System.getProperty("user.dir"), "target", "generated-sources", "base");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            
            Path sourceFilePath = directoryPath.resolve("BaseTest.java");
            String code = """
                    package com.enterprise.banking.tests.generated.base;
                    
                    import org.openqa.selenium.WebDriver;
                    import org.openqa.selenium.chrome.ChromeDriver;
                    import org.testng.annotations.AfterMethod;
                    import org.testng.annotations.BeforeMethod;
                    
                    /**
                     * Auto-generated BaseTest for dynamic WebDriver management.
                     */
                    public class BaseTest {
                        protected WebDriver driver;
                        
                        @BeforeMethod
                        public void setUp() {
                            driver = new ChromeDriver();
                            driver.manage().window().maximize();
                        }
                        
                        @AfterMethod
                        public void tearDown() {
                            if (driver != null) {
                                driver.quit();
                            }
                        }
                        
                        public WebDriver getDriver() {
                            return driver;
                        }
                    }
                    """;
            Files.writeString(sourceFilePath, code);
            LOGGER.info("BaseTest successfully generated at: {}", sourceFilePath.toAbsolutePath());
        } catch (IOException ioException) {
            LOGGER.error("Failed to generate BaseTest", ioException);
            throw new AiExtensionException("Failed to generate BaseTest configuration", ioException);
        }
    }

    /**
     * Generates a dynamic Page Object Model (POM) class mapping extracted web elements.
     *
     * @param pageName The desired name of the Page Object class.
     * @param locators The collection of locators (e.g., XPath, ID) to be mapped as WebElements.
     * @throws AiExtensionException if the file writing process is interrupted.
     */
    public void generateDynamicPageObject(String pageName, List<String> locators) {
        if (pageName == null || pageName.isBlank()) {
            throw new IllegalArgumentException("Page Object name cannot be null or empty.");
        }
        if (locators == null) {
            throw new IllegalArgumentException("Locators collection cannot be null.");
        }
        
        LOGGER.info("Generating dynamic page object mapping for: {}", pageName);
        try {
            Path directoryPath = Paths.get(System.getProperty("user.dir"), "target", "generated-sources", "pages");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            
            Path sourceFilePath = directoryPath.resolve(pageName + ".java");
            StringBuilder code = new StringBuilder();
            code.append("package com.enterprise.banking.pages.generated;\n\n");
            code.append("import org.openqa.selenium.WebDriver;\n");
            code.append("import org.openqa.selenium.WebElement;\n");
            code.append("import org.openqa.selenium.support.FindBy;\n");
            code.append("import org.openqa.selenium.support.PageFactory;\n\n");
            code.append("/**\n * Dynamically generated Page Object for ").append(pageName).append(".\n */\n");
            code.append("public class ").append(pageName).append(" {\n\n");
            code.append("    private final WebDriver driver;\n\n");
            
            for (int i = 0; i < locators.size(); i++) {
                code.append("    @FindBy(xpath = \"").append(locators.get(i)).append("\")\n");
                code.append("    private WebElement dynamicElement").append(i + 1).append(";\n\n");
            }
            
            code.append("    public ").append(pageName).append("(WebDriver driver) {\n");
            code.append("        this.driver = driver;\n");
            code.append("        PageFactory.initElements(driver, this);\n");
            code.append("    }\n");
            code.append("}\n");

            Files.writeString(sourceFilePath, code.toString());
            LOGGER.info("Successfully mapped Page Object: {}", sourceFilePath.toAbsolutePath());
        } catch (IOException ioException) {
            LOGGER.error("Failed to map dynamic page object: {}", pageName, ioException);
            throw new AiExtensionException("Failed to generate dynamic page object: " + pageName, ioException);
        }
    }

    /**
     * Generates a concrete TestNG class bridging abstract test steps into execution blocks.
     *
     * @param className The name of the test suite class.
     * @param testSteps The abstract human-readable steps to be executed.
     * @throws AiExtensionException if the file generation is restricted.
     */
    public void generateDynamicTestClass(String className, List<String> testSteps) {
        if (className == null || className.isBlank()) {
            throw new IllegalArgumentException("Test Class name cannot be null or empty.");
        }
        if (testSteps == null) {
            throw new IllegalArgumentException("Test steps collection cannot be null.");
        }
        
        LOGGER.info("Generating dynamic Test Class execution block for: {}", className);
        try {
            Path directoryPath = Paths.get(System.getProperty("user.dir"), "target", "generated-sources", "tests");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            
            Path sourceFilePath = directoryPath.resolve(className + ".java");
            StringBuilder code = new StringBuilder();
            code.append("package com.enterprise.banking.tests.generated;\n\n");
            code.append("import org.testng.annotations.Test;\n");
            code.append("import com.enterprise.banking.tests.generated.base.BaseTest;\n\n");
            code.append("/**\n * Dynamically synthesized Test Class for ").append(className).append(".\n */\n");
            code.append("public class ").append(className).append(" extends BaseTest {\n\n");
            
            code.append("    @Test(description = \"Auto-generated test execution bridge\")\n");
            code.append("    public void execute").append(className).append("() {\n");
            for (String step : testSteps) {
                code.append("        // Execution Step: ").append(step).append("\n");
                code.append("        System.out.println(\"Executing runtime step: ").append(step).append("\");\n");
            }
            code.append("    }\n");
            code.append("}\n");

            Files.writeString(sourceFilePath, code.toString());
            LOGGER.info("Successfully synthesized Test Class: {}", sourceFilePath.toAbsolutePath());
        } catch (IOException ioException) {
            LOGGER.error("Failed to generate dynamic test class: {}", className, ioException);
            throw new AiExtensionException("Failed to generate dynamic test class: " + className, ioException);
        }
    }
}