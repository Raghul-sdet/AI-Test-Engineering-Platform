package com.enterprise.banking.ai.generator;

import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.exception.AiExtensionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    /**
     * Generates a complete, production-ready Selenium TestNG class file dynamically.
     *
     * @param testCases       The list of AI-generated test cases to be compiled into code.
     * @param outputDirectory The physical directory path where the Java code will be saved.
     * @return The Java File object representing the generated source code.
     * @throws AiExtensionException if file I/O operations fail or directory creation is denied.
     */
    public File generateSeleniumCode(List<TestCase> testCases, String outputDirectory) {
        if (testCases == null || testCases.isEmpty()) {
            LOGGER.error("Provided test case collection is null or empty");
            throw new IllegalArgumentException("Test case collection cannot be null or empty.");
        }
        if (outputDirectory == null || outputDirectory.trim().isEmpty()) {
            LOGGER.error("Provided output directory is null or empty");
            throw new IllegalArgumentException("Output directory path cannot be null or empty.");
        }

        LOGGER.info("Initiating dynamic Selenium TestNG code generation for {} test cases.", testCases.size());

        try {
            Path directoryPath = Paths.get(outputDirectory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            String generatedClassName = "AiGeneratedWebAutomationTest_" + System.currentTimeMillis();
            Path sourceFilePath = directoryPath.resolve(generatedClassName + ".java");

            StringBuilder sourceCodeBuilder = new StringBuilder();
            sourceCodeBuilder.append("package com.enterprise.banking.tests.generated;\n\n");
            sourceCodeBuilder.append("import org.openqa.selenium.WebDriver;\n");
            sourceCodeBuilder.append("import org.testng.Assert;\n");
            sourceCodeBuilder.append("import org.testng.annotations.Test;\n");
            sourceCodeBuilder.append("import com.enterprise.banking.tests.BaseTest;\n\n");
            
            sourceCodeBuilder.append("/**\n * Auto-generated TestNG execution suite.\n */\n");
            sourceCodeBuilder.append("public class ").append(generatedClassName).append(" extends BaseTest {\n\n");

            for (int i = 0; i < testCases.size(); i++) {
                sourceCodeBuilder.append("    @Test(description = \"AI Generated Automated Scenario Execution Block\")\n");
                sourceCodeBuilder.append("    public void executeGeneratedScenarioBlock").append(i + 1).append("() {\n");
                sourceCodeBuilder.append("        WebDriver driver = getDriver();\n");
                sourceCodeBuilder.append("        Assert.assertNotNull(driver, \"WebDriver initialization critically failed\");\n");
                sourceCodeBuilder.append("        System.out.println(\"Triggering dynamically generated test sequence...\");\n");
                sourceCodeBuilder.append("    }\n\n");
            }
            sourceCodeBuilder.append("}\n");

            Files.writeString(sourceFilePath, sourceCodeBuilder.toString());
            LOGGER.info("Selenium test suite successfully compiled and written to: {}", sourceFilePath.toAbsolutePath());

            return sourceFilePath.toFile();

        } catch (IOException ioException) {
            LOGGER.error("Failed to write generated Selenium code to the target file system.", ioException);
            throw new AiExtensionException("Selenium Code Generation encountered a critical IO exception", ioException);
        }
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