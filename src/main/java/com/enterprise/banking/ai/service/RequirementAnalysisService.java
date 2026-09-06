package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.analysis.CoverageAnalyzer;
import com.enterprise.banking.ai.analysis.RequirementAnalyzer;
import com.enterprise.banking.ai.analysis.RiskAnalyzer;
import com.enterprise.banking.ai.exception.RequirementAnalysisException;
import com.enterprise.banking.ai.model.Requirement;

import java.util.logging.Logger;

/**
 * Main orchestrator service that coordinates requirement parsing, risk detection,
 * and coverage calculation via dedicated analysis engine components.
 */
public class RequirementAnalysisService {

    private static final Logger LOGGER = Logger.getLogger(RequirementAnalysisService.class.getName());

    private final RequirementAnalyzer requirementAnalyzer;
    private final RiskAnalyzer riskAnalyzer;
    private final CoverageAnalyzer coverageAnalyzer;

    /**
     * Initializes the service with its requisite analysis components.
     */
    public RequirementAnalysisService() {
        this.requirementAnalyzer = new RequirementAnalyzer();
        this.riskAnalyzer = new RiskAnalyzer();
        this.coverageAnalyzer = new CoverageAnalyzer();
    }

    /**
     * Executes the complete AI requirement analysis pipeline.
     *
     * @param rawRequirement Business requirement text
     * @return Fully populated Requirement Object
     * @throws RequirementAnalysisException if processing fails
     */
    public Requirement processRequirement(String rawRequirement) {
        try {
            LOGGER.info("Initiating Requirement Analysis Pipeline.");
            
            Requirement requirement = requirementAnalyzer.extractRequirementStructure(rawRequirement);
            LOGGER.info("Requirement structure extracted. ID: " + requirement.getId());

            requirement.setRisks(riskAnalyzer.detectRisks(requirement));
            LOGGER.info("Risk analysis completed. Identified risks: " + requirement.getRisks().size());

            requirement.setCoverage(coverageAnalyzer.calculateCoverage(requirement));
            LOGGER.info("Coverage analysis completed.");

            return requirement;
            
        } catch (Exception ex) {
            LOGGER.severe("Failed to process requirement: " + ex.getMessage());
            throw new RequirementAnalysisException("Requirement Pipeline Execution Failed", ex);
        }
    }
}