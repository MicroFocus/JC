package com.hpe.jc.plugins;

import com.hpe.jc.JCPlugin;
import com.hpe.jc.errors.GherkinAssert;
import com.hpe.jc.gherkin.*;
import gherkin.lexer.En;
import gherkin.lexer.Lexer;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koreny on 3/31/2017.
 */
public class JCPValidateFlowBy extends JCPlugin {

    /*********************************
     * Members
     *********************************/

    public static final String EXPECTED_FEATURE = "EXPECTED_FEATURE";
    public static final String EXPECTED_SCRIPT = "EXPECTED_SCRIPT";
    public static final String EXPECTED_TO_ACTUAL_SCENARIO_MAP = "EXPECTED_TO_ACTUAL_SCENARIO_MAP";
    public static final String EXPECTED_TO_ACTUAL_STEP_MAP = "EXPECTED_TO_ACTUAL_STEP_MAP";
    public static final String SCRIPT_URL = "SCRIPT_URL";

    // contains the feature file data (taken from the @feature annotation)
    protected GherkinFeature expectedFeature;

    // used to link to expected scenario and step from feature definition. Updated in start of scenario / step.
    private GherkinScenario expectedScenario;
    private GherkinStep expectedStep;

    String expectedScript;
    String featureFileLocation;
    HashMap<GherkinScenario, GherkinScenario> expectedScenarioToActualMap = new HashMap<>();
    HashMap<GherkinStep, GherkinStep> expectedStepToActualMap = new HashMap<>();


    /*********************************
     * Constructors
     *********************************/

    protected JCPValidateFlowBy() {

    }

    public JCPValidateFlowBy(String featureFileLocation) {
        this.featureFileLocation = featureFileLocation;
    }

    /*********************************
     * Plugin getters
     *********************************/

    public static GherkinFeature getExpectedFeature(GherkinFeature feature) {
        return (GherkinFeature)feature.getData(JCPValidateFlowBy.class, EXPECTED_FEATURE);
    }

    public static String getExpectedScript(GherkinFeature feature) {
        return (String)feature.getData(JCPValidateFlowBy.class, EXPECTED_SCRIPT);
    }

    public static HashMap<GherkinScenario, GherkinScenario> getExpectedToActualScenarioMap(GherkinFeature feature) {
        return (HashMap<GherkinScenario, GherkinScenario>)feature.getData(JCPValidateFlowBy.class, EXPECTED_TO_ACTUAL_SCENARIO_MAP);
    }

    public static HashMap<GherkinStep, GherkinStep> getExpectedToActualStepMap(GherkinFeature feature) {
        return (HashMap<GherkinStep, GherkinStep>)feature.getData(JCPValidateFlowBy.class, EXPECTED_TO_ACTUAL_STEP_MAP);
    }

    public static String getFeatureFileLocation(GherkinFeature feature) {
        return (String)feature.getData(JCPValidateFlowBy.class, SCRIPT_URL);
    }


    /*********************************
     * Private methods
     *********************************/


    private static String readGherkinScript(Object testObj, String featureFileLocation) {

        InputStream stream = testObj.getClass().getResourceAsStream(featureFileLocation);
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";

/*
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(featureFileLocation)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
*/
    }

    protected static GherkinFeature parseGherkinScript(String script) {
        GherkinLexerListener listener = new GherkinLexerListener();
        Lexer lexer = new En(listener);
        lexer.scan(script);

        return listener.currentFeature;
    }

    /*********************************
     * Plugin implementation
     *********************************/

    @Override
    protected void onInit() {
        // load and parse gherkin script

        expectedScript = readGherkinScript(progress.getTestObject(), featureFileLocation);
        expectedFeature = parseGherkinScript(expectedScript);
    }


    @Override
    protected void onEndOfAny() {

    }

    @Override
    protected void onStartOfAny() {

    }

    protected void onFeatureStart() {
        progress.getCurrentFeature().setData(this.getClass(), EXPECTED_FEATURE, expectedFeature);
        progress.getCurrentFeature().setData(this.getClass(), EXPECTED_SCRIPT, expectedScript);
        progress.getCurrentFeature().setData(this.getClass(), SCRIPT_URL, featureFileLocation);

        GherkinAssert.SameFeature(expectedFeature, progress.getCurrentFeature());
    }

    protected void onFeatureEnd() {
        progress.getCurrentFeature().setData(this.getClass(), EXPECTED_TO_ACTUAL_SCENARIO_MAP, expectedScenarioToActualMap);
        progress.getCurrentFeature().setData(this.getClass(), EXPECTED_TO_ACTUAL_STEP_MAP, expectedStepToActualMap);
        GherkinAssert.sameNumberOfScenarios(expectedFeature, progress, expectedScenarioToActualMap);
    }

    protected void onScenarioStart() {
        expectedScenario = GherkinAssert.featureContainsScenario(expectedFeature, progress.getCurrentScenario());
        expectedScenarioToActualMap.put(expectedScenario, progress.getCurrentScenario());
        expectedStep = null;
    }

    protected void onScenarioEnd() {
        boolean noMeaningfulExceptions =
                progress.getCurrentScenario().getFatalExceptions().size()==0 &&
                progress.getCurrentScenario().getTestExceptions().size()==0;

        // if there were exceptions - no point checking scenario, as it most probably didn't run.
        if (noMeaningfulExceptions) {
            GherkinAssert.ScenarioHasAllSteps(expectedScenario, progress.getCurrentScenario());
        }
        expectedScenario = null;
    }

    protected void onStepStart() {
        GherkinStep expectedStep = expectedScenario.getNextStep(this.expectedStep);

        // step is not null
        GherkinAssert.validExpectedNextStep(progress, expectedScenario, expectedStep);

        // we link the steps before validating expected step because if validation fails, error will be logged at current steps and we want access to them from this expected step
        expectedStepToActualMap.put(expectedStep, progress.getCurrentStep());

        // now validating correct actual step
        GherkinAssert.nextStepIsAsExpected(progress, expectedScenario, expectedStep);

        // update link if everything is ok
        this.expectedStep = expectedStep;
    }

    protected void onStepEnd() {

    }


    protected void onStepFailure(Throwable ex) {

    }
}
