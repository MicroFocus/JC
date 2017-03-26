/**
 * Created by koreny on 3/20/2017.
 */

package com.hpe;

import gherkin.lexer.En;
import gherkin.lexer.Lexer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Feature {

    /*********************************
     * Private properties
     *********************************/

    // used to:
    // 1. track progress of test
    // 2. compare against real feature file
    private GherkinProgress progress;


    /*********************************
     * Private methods
     *********************************/

    private static String readGherkinScript(String scriptPath) {

        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(scriptPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    private static GherkinFeature parseGherkinScript(String script) {
        GherkinLexerListener listener = new GherkinLexerListener();
        Lexer lexer = new En(listener);
        lexer.scan(script);

        return listener.currentFeature;
    }


    /*********************************
     * Constructors methods - Feature definitions
     *********************************/

    // Using this constructor will compare between feature file and actual scenarios and steps
    public Feature(Object test, String featureFileLocation, String featureDescription) {

        progress = new GherkinProgress(test);

        // load and parse gherkin script
        String gherkinScript = readGherkinScript(featureFileLocation);
        GherkinFeature feature = parseGherkinScript(gherkinScript);
        progress.setFeatureData(feature);

        // update the progress with the feature
        GherkinFeature actualFeature = new GherkinFeature(featureDescription);
        progress.updateFeature(actualFeature);
    }


    // Using this constructor will only create a report of what happened
    public Feature(Object test, String featureDescription) {

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
        } catch (Exception | Error e) {
            progress.updateException(e);
            throw e;
        } finally {
            // update only if everything is ok
            if (!progress.isCurrentScenarioHasException()) {
                progress.updateScenario(null);
            }
            progress.report();
        }
    }

    // todo - handle parameters (in scenario too), running it several times
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

    public void given(String stepDesc) {
        GherkinStep step = new GherkinStep("given", stepDesc);
        progress.updateStep(step);
    }

    public void when(String stepDesc) {
        GherkinStep step = new GherkinStep("when", stepDesc);
        progress.updateStep(step);
    }

    public void then(String stepDesc) {
        GherkinStep step = new GherkinStep("then", stepDesc);
        progress.updateStep(step);
    }

    public void and(String stepDesc) {
        GherkinStep step = new GherkinStep("and", stepDesc);
        progress.updateStep(step);
    }

    public void but(String stepDesc) {
        GherkinStep step = new GherkinStep("but", stepDesc);
        progress.updateStep(step);
    }

    public void finished() {
        int a = 6;
    }
}
