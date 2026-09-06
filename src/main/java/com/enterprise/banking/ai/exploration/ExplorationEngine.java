package com.enterprise.banking.ai.exploration;

import com.enterprise.banking.ai.exploration.actions.ActionHistory;
import com.enterprise.banking.ai.exploration.actions.ActionPlanner;
import com.enterprise.banking.ai.exploration.actions.ActionValidator;
import com.enterprise.banking.ai.exploration.anomaly.BrokenFlowDetector;
import com.enterprise.banking.ai.exploration.anomaly.InfiniteLoopDetector;
import com.enterprise.banking.ai.exploration.discovery.ComponentDiscoveryEngine;
import com.enterprise.banking.ai.exploration.discovery.ElementDiscoveryEngine;
import com.enterprise.banking.ai.exploration.discovery.StateDiscoveryEngine;
import com.enterprise.banking.ai.exploration.learning.LearningEngine;
import com.enterprise.banking.ai.exploration.navigation.NavigationNode;
import com.enterprise.banking.ai.exploration.navigation.NavigationPlanner;
import com.enterprise.banking.ai.exploration.navigation.NavigationRecorder;
import com.enterprise.banking.ai.exploration.navigation.NavigationTracker;
import com.enterprise.banking.ai.exploration.report.CoverageExpansionReport;
import com.enterprise.banking.ai.exploration.report.ExplorationReport;
import com.enterprise.banking.ai.exploration.report.ExplorationStatistics;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * The Master Controller implementing the continuous, autonomous exploration lifecycle.
 */
public class ExplorationEngine {
    private static final Logger LOGGER = Logger.getLogger(ExplorationEngine.class.getName());

    private final NavigationTracker tracker;
    private final ExplorationPlanner planner;
    private final ExplorationExecutor executor;
    private final LearningEngine learningEngine;
    private final StateDiscoveryEngine stateDiscovery;
    private final ElementDiscoveryEngine elementDiscovery;
    private final ComponentDiscoveryEngine componentDiscovery;
    private final InfiniteLoopDetector loopDetector;
    private final BrokenFlowDetector crashDetector;
    private final ActionValidator validator;
    private final NavigationRecorder recorder;

    public ExplorationEngine() {
        this.tracker = new NavigationTracker();
        ActionHistory actionHistory = new ActionHistory();
        
        NavigationPlanner navPlanner = new NavigationPlanner(this.tracker);
        ActionPlanner actPlanner = new ActionPlanner();
        this.planner = new ExplorationPlanner(navPlanner, actPlanner);
        
        this.executor = new ExplorationExecutor(actionHistory);
        this.learningEngine = new LearningEngine();
        this.stateDiscovery = new StateDiscoveryEngine();
        this.elementDiscovery = new ElementDiscoveryEngine();
        this.componentDiscovery = new ComponentDiscoveryEngine();
        this.loopDetector = new InfiniteLoopDetector();
        this.crashDetector = new BrokenFlowDetector();
        this.validator = new ActionValidator();
        this.recorder = new NavigationRecorder();
    }

    /**
     * Commences a bounded autonomous exploration session against a target application URL.
     *
     * @param context Instructions and limits for the session
     * @return ExplorationResult containing artifacts and metrics
     */
    public ExplorationResult runExploration(ExplorationContext context) {
        ExplorationSession session = new ExplorationSession(context);
        session.setState(ExplorationState.DISCOVERING);
        LOGGER.info("Starting Autonomous Exploration Sequence. Target: " + context.getStartUrl());

        int currentDepth = 0;
        int anomalies = 0;

        while (currentDepth < context.getMaxDepth() && session.getState() != ExplorationState.FAILED) {

            // Simulated DOM state for architectural flow. Varies per depth so that
            // PageDiscoveryEngine's domHash (= domSource.hashCode()) differs per page, matching
            // NavigationNode.equals() which compares only domHash. Previously this was a single
            // constant string declared once outside the loop, so every simulated page hashed
            // identically and InfiniteLoopDetector (which compares nodes by equals()) false-
            // positived an infinite loop after just 4 iterations on every single run, regardless
            // of maxDepth or actual progress.
            String mockDom = "<html><form><input/><button/></form><!-- depth:" + currentDepth + " --></html>";

            // 1. Discover State
            NavigationNode currentNode = stateDiscovery.identifyState(context.getStartUrl() + "/path" + currentDepth, "Page " + currentDepth, mockDom);
            tracker.updateCurrentState(currentNode, "AUTO_NAVIGATE");
            
            // 2. Analyze Anomalies
            if (crashDetector.detect(mockDom)) {
                LOGGER.severe("Broken Flow Anomaly Detected. Halting.");
                session.setState(ExplorationState.FAILED);
                anomalies++;
                break;
            }
            if (loopDetector.detectLoop(tracker.getHistory().getVisitedPath())) {
                LOGGER.warning("Infinite Loop Detected. Aborting Path.");
                session.setState(ExplorationState.ABORTED_INFINITE_LOOP);
                break;
            }

            // 3. Extract Elements & Components
            List<String> interactables = elementDiscovery.discoverInteractables(mockDom);
            List<String> components = componentDiscovery.discoverComponents(mockDom);
            interactables.addAll(components);

            if (interactables.isEmpty()) {
                session.setState(ExplorationState.ABORTED_DEAD_END);
                break;
            }

            // 4. Plan Action
            String nextAction = planner.getActionPlanner().planNextAction(interactables);
            
            if (validator.isSafeToExecute(nextAction)) {
                // 5. Execute Action
                executor.executeStep(nextAction);
                recorder.recordTransition(currentNode, null, nextAction);
                
                // 6. Learn
                learningEngine.evaluateState(tracker.getGraph(), nextAction);
            }

            currentDepth++;
        }

        if (session.getState() == ExplorationState.DISCOVERING || session.getState() == ExplorationState.NAVIGATING) {
            session.setState(ExplorationState.COMPLETED);
        }

        LOGGER.info("Exploration Session Concluded. Final State: " + session.getState());
        
        ExplorationStatistics stats = new ExplorationStatistics(
                tracker.getGraph().getNodes().size(), 
                currentDepth, 
                anomalies, 
                currentDepth * 10.0
        );
        CoverageExpansionReport expansionReport = new CoverageExpansionReport(Collections.singletonList("/new-discovered-path"));
        ExplorationReport report = new ExplorationReport(context, session.getState(), stats, expansionReport);

        return new ExplorationResult(session, report);
    }
}