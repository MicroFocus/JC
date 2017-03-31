package com.hpe;

import gherkin.lexer.En;
import gherkin.lexer.Lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koreny on 3/31/2017.
 */
public class JCPFeatureFileVsActualValidator extends JCPlugin {

    // contains the feature file data (taken from the @feature annotation)
    private GherkinFeature featureFile;

    // used to link to expected scenario and step from feature definition. Updated in start of scenario / step.
    private GherkinScenario linkToScenarioDefinition;
    private GherkinStep linkToStepDefinition;

    HashMap<GherkinScenario, GherkinScenario> file2actual = new HashMap<GherkinScenario, GherkinScenario>();

    public JCPFeatureFileVsActualValidator(String featureFileLocation) {

        // load and parse gherkin script
        String gherkinScript = readGherkinScript(featureFileLocation);
        GherkinFeature featureFile = parseGherkinScript(gherkinScript);

        this.featureFile = featureFile;
    }


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


    @Override
    public void onFeatureStart(GherkinProgress progress) {
        GherkinAssert.SameFeature(featureFile, progress.getCurrentFeature());
    }

    @Override
    public void onFeatureEnd(GherkinProgress progress) {
        GherkinAssert.sameNumberOfScenarios(featureFile, progress, file2actual);
    }

    @Override
    public void onScenarioStart(GherkinProgress progress) {
        linkToScenarioDefinition = GherkinAssert.featureContainsScenario(featureFile, progress.getCurrentScenario());
        file2actual.put(linkToScenarioDefinition, progress.getCurrentScenario());
        linkToStepDefinition = null;
    }

    @Override
    public void onScenarioEnd(GherkinProgress progress) {
        GherkinAssert.ScenarioHasAllSteps(linkToScenarioDefinition, progress.getCurrentScenario());
        linkToScenarioDefinition = null;
    }

    @Override
    public void onStepStart(GherkinProgress progress) {
        GherkinStep expectedStep = linkToScenarioDefinition.getNextStep(linkToStepDefinition);
        GherkinAssert.validExpectedNextStep(progress, linkToScenarioDefinition, expectedStep);
        GherkinAssert.nextStepIsAsExpected(progress, linkToScenarioDefinition, expectedStep);

        // update link if everything is ok
        linkToStepDefinition = expectedStep;
    }

    @Override
    public void onStepEnd(GherkinProgress progress) {

    }

    @Override
    public void onStepFailure(GherkinProgress progress, Throwable ex) {

    }
}