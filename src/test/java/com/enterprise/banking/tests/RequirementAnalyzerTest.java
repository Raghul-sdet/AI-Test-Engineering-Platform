package com.enterprise.banking.tests;

import com.enterprise.banking.ai.exception.RequirementAnalysisException;
import com.enterprise.banking.ai.model.Requirement;
import com.enterprise.banking.ai.model.RequirementPriority;
import com.enterprise.banking.ai.service.RequirementAnalysisService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test to validate the AI Requirement Analysis Engine.
 */
public class RequirementAnalyzerTest {

    private RequirementAnalysisService analysisService;

    @BeforeMethod
    public void setUp() {
        analysisService = new RequirementAnalysisService();
    }

    @Test
    public void testCompleteRequirementAnalysisPipeline() {
        String rawRequirement = """
                Banking Transfer Module Redesign
                
                This requirement outlines the urgent need to modify the legacy transfer system.
                
                Feature: The user can authenticate securely before making a transfer.
                
                Feature: The user can initiate a payment transaction across borders.
                
                This update impacts the core ledger and requires extensive validation.
                """;

        Requirement analyzedRequirement = analysisService.processRequirement(rawRequirement);

        // Asserts
        Assert.assertNotNull(analyzedRequirement, "Requirement object should not be null");
        Assert.assertNotNull(analyzedRequirement.getId(), "Requirement ID should be generated");
        Assert.assertEquals(analyzedRequirement.getTitle(), "Banking Transfer Module Redesign", "Title should be extracted correctly");
        
        Assert.assertTrue(analyzedRequirement.getFeatures().size() >= 2, "Should extract at least 2 features");
        Assert.assertEquals(analyzedRequirement.getPriority(), RequirementPriority.CRITICAL, "Priority should be critical due to 'urgent' keyword");

        Assert.assertNotNull(analyzedRequirement.getRisks(), "Risks list should not be null");
        Assert.assertTrue(analyzedRequirement.getRisks().size() > 0, "Risks should be identified");
        
        boolean hasSecurityRisk = analyzedRequirement.getRisks().stream()
                .anyMatch(risk -> risk.getRiskType().equals("Security Impact"));
        Assert.assertTrue(hasSecurityRisk, "Security risk should be detected");

        Assert.assertNotNull(analyzedRequirement.getCoverage(), "Coverage object should not be null");
        Assert.assertTrue(analyzedRequirement.getCoverage().getFunctionalCoverage() > 0, "Functional coverage should be calculated");
        Assert.assertTrue(analyzedRequirement.getCoverage().getBusinessCoverage() > 0, "Business coverage should be calculated");
        Assert.assertTrue(analyzedRequirement.getCoverage().getOverallCoverage() > 0, "Overall coverage should be calculated");
    }

    @Test(expectedExceptions = RequirementAnalysisException.class)
    public void testNullRequirementThrowsException() {
        analysisService.processRequirement(null);
    }
    
    @Test(expectedExceptions = RequirementAnalysisException.class)
    public void testEmptyRequirementThrowsException() {
        analysisService.processRequirement("   ");
    }
}