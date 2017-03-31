/**
 * Created by koreny on 3/20/2017.
 */

package com.hpe;

import gherkin.lexer.En;
import gherkin.lexer.Lexer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JC {

    /*********************************
     * Private properties
     *********************************/

    // used to:
    // 1. track progress of test
    // 2. compare against real feature file
    private GherkinProgress progress;


    /*********************************
     * Constructors methods - JC definitions
     *********************************/

    // Using this constructor will compare between feature file and actual scenarios and steps
    public JC(Object test, JCPlugin[] plugins, String featureDescription) {

        progress = new GherkinProgress(test, plugins);

        // update the progress with the feature
        GherkinFeature feature = new GherkinFeature(featureDescription);
        progress.updateFeature(feature);
    }


    // Using this constructor will only create a report of what happened
    public JC(Object test, String featureDescription) {

        progress = new GherkinProgress(test);

        // update the progress with the feature
        GherkinFeature actualFeature = new GherkinFeature(featureDescription);
        progress.updateFeature(actualFeature);
    }


    /*********************************
     * Public methods - Scenario and Step types
     *********************************/

    // init scenario level
    public void scenario(String description, Runnable code) {

        // update progress with scenario
        GherkinScenario scenario = new GherkinScenario(description);
        progress.updateScenario(scenario);

        // handle running the scenario
        try {
            code.run();
        } catch (JCCannotContinueException e) {
            progress.updateException(e);
            throw e;
        } catch (Exception | Error e) {
            progress.updateException(e);
            throw GherkinAssert.createClearExceptionFrom(e, progress);
        } finally {
            // update only if everything is ok
            if (!progress.isCurrentScenarioHasException()) {
                progress.updateScenario(null);
            }
            progress.report();
        }
    }

    // todo - handle parameters (in scenario too), running it several times
    /*
    public void scenarioOutline(String description, Runnable code) {
        try {
            code.run();
        } catch (Exception e) {
            throw e;
        }
    }

    // todo - implement
    public void background(String description, Runnable code) {
        try {
            code.run();
        } catch (Exception e) {
            throw e;
        }
    }
    */
    public void given(String stepDesc) {
        GherkinStep step = new GherkinStep("Given", stepDesc);
        progress.updateStep(step);
    }

    public void when(String stepDesc) {
        GherkinStep step = new GherkinStep("When", stepDesc);
        progress.updateStep(step);
    }

    public void then(String stepDesc) {
        GherkinStep step = new GherkinStep("Then", stepDesc);
        progress.updateStep(step);
    }

    public void and(String stepDesc) {
        GherkinStep step = new GherkinStep("And", stepDesc);
        progress.updateStep(step);
    }

    public void but(String stepDesc) {
        GherkinStep step = new GherkinStep("But", stepDesc);
        progress.updateStep(step);
    }

    public void finished() {
        progress.updateFeature(null);
    }
}
