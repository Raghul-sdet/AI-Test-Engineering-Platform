package com.enterprise.banking.ai.exploration.learning;

import com.enterprise.banking.ai.exploration.navigation.NavigationGraph;
import java.util.logging.Logger;

/**
 * Orchestrator for continuous learning analysis running asynchronously against the active session.
 */
public class LearningEngine {
    private static final Logger LOGGER = Logger.getLogger(LearningEngine.class.getName());
    
    private final BehaviorMemory memory;
    private final ExplorationKnowledgeBase knowledgeBase;
    private final PatternLearner patternLearner;
    private final CoverageLearner coverageLearner;

    public LearningEngine() {
        this.memory = new BehaviorMemory();
        this.knowledgeBase = new ExplorationKnowledgeBase();
        this.patternLearner = new PatternLearner();
        this.coverageLearner = new CoverageLearner();
    }

    public void evaluateState(NavigationGraph currentGraph, String lastAction) {
        LOGGER.info("Learning Engine evaluating exploration progression.");
        knowledgeBase.recordSuccess(lastAction);
        patternLearner.learnFromSequence(lastAction);
        
        double coverage = coverageLearner.calculateExplorationCoverage(currentGraph);
        if (coverage > 80.0) {
            memory.addInsight("Application highly traversed. Most functional paths mapped.");
        }
    }

    public BehaviorMemory getMemory() { return memory; }
}