package com.enterprise.banking.ai.mapper;

import com.enterprise.banking.ai.dom.model.DOMElement;
import com.enterprise.banking.ai.generator.DataProviderSkeletonGenerator;
import com.enterprise.banking.ai.generator.ParameterResolver;
import com.enterprise.banking.ai.generator.pageobject.PageObjectMethodGenerator;
import com.enterprise.banking.ai.generator.test.TestClassGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * Core engine integrating AI Test Design steps with the DOM Intelligence repository
 * to orchestrate executable code generation for both Page and Test classes.
 */
public class ActionMappingEngine {

    private final SemanticMatcher matcher = new SemanticMatcher();
    private final ParameterResolver parameterResolver = new ParameterResolver();
    private final PageObjectMethodGenerator codeGenerator = new PageObjectMethodGenerator();
    private final DataProviderSkeletonGenerator dataProviderGenerator = new DataProviderSkeletonGenerator();
    private final TestClassGenerator testGenerator = new TestClassGenerator();

    public void executeMappingProtocol(List<DOMElement> domRepository) throws Exception {
        System.out.println("\n[MAPPING-ENGINE] Initiating Action Mapping Engine...");
        
        List<MappedAction> mappedActions = new ArrayList<>();
        int skippedSteps = 0;

        // Simulated steps parsed from AI_Test_Design.xlsx
        String[][] excelSteps = {
            {"enterUsername", "Enter Username", "Username=testuser"},
            {"clickLogin", "Click Login Button", ""}
        };

        for (String[] row : excelSteps) {
            String methodNameTarget = row[0];
            String actionSentence = row[1];
            String testData = row[2];

            DOMElement bestMatch = null;
            double highestScore = 0.0;

            for (DOMElement element : domRepository) {
                double score = matcher.calculateConfidence(actionSentence, element);
                if (score > highestScore) {
                    highestScore = score;
                    bestMatch = element;
                }
            }

            if (bestMatch != null && highestScore >= 0.4) {
                String actionType = parameterResolver.determineActionType(actionSentence);
                List<String> params = parameterResolver.resolveParameters(actionSentence, testData);
                
                mappedActions.add(new MappedAction(methodNameTarget, actionType, bestMatch, params, highestScore));
                System.out.printf("[MAPPING-ENGINE] MATCHED: '%s' -> DOM Element: '%s' (Confidence: %.2f)%n", 
                                  actionSentence, bestMatch.elementName(), highestScore);
            } else {
                skippedSteps++;
                System.out.println("[MAPPING-ENGINE] SKIPPED: Could not reliably map '" + actionSentence + "'");
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.println("Total Steps Mapped  : " + mappedActions.size());
        System.out.println("Total Steps Skipped : " + skippedSteps);
        System.out.println("--------------------------------------------------");

        if (!mappedActions.isEmpty()) {
            // Generate the Page Object
            codeGenerator.generateExecutablePageObject("IntelligentLoginPage", mappedActions);
            
            // Generate the TestNG Test Class
            testGenerator.generateTestClass("IntelligentLoginTest", "IntelligentLoginPage", mappedActions);
            
            // Generate Data Provider Skeleton
            dataProviderGenerator.generateSkeleton();
            
            System.out.println("[MAPPING-ENGINE] Code Generation Complete. Page and Test Objects created.");
        }
    }
}