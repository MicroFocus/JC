package com.microfocus.jc.plugins;

import com.microfocus.jc.JCCannotContinueException;
import com.microfocus.jc.JCPlugin;
import com.microfocus.jc.errors.GherkinAssert;
import com.microfocus.jc.gherkin.GherkinFeature;
import com.microfocus.jc.gherkin.GherkinScenario;
import com.microfocus.jc.gherkin.GherkinStep;
import gherkin.lexer.En;
import gherkin.lexer.Lexer;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by koreny on 3/31/2017.
 */
public class JCPFeatureFileValidator extends JCPlugin {

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

    protected JCPFeatureFileValidator() {

    }

    public JCPFeatureFileValidator(String featureFileLocation) {
        this.featureFileLocation = featureFileLocation;
    }

    /*********************************
     * Plugin getters
     *********************************/

    public static GherkinFeature getExpectedFeature(GherkinFeature feature) {
        return (GherkinFeature)feature.getData(JCPFeatureFileValidator.class, EXPECTED_FEATURE);
    }

    public static String getExpectedScript(GherkinFeature feature) {
        return (String)feature.getData(JCPFeatureFileValidator.class, EXPECTED_SCRIPT);
    }

    public static HashMap<GherkinScenario, GherkinScenario> getExpectedToActualScenarioMap(GherkinFeature feature) {
        return (HashMap<GherkinScenario, GherkinScenario>)feature.getData(JCPFeatureFileValidator.class, EXPECTED_TO_ACTUAL_SCENARIO_MAP);
    }

    public static HashMap<GherkinStep, GherkinStep> getExpectedToActualStepMap(GherkinFeature feature) {
        return (HashMap<GherkinStep, GherkinStep>)feature.getData(JCPFeatureFileValidator.class, EXPECTED_TO_ACTUAL_STEP_MAP);
    }

    public static String getFeatureFileLocation(GherkinFeature feature) {
        return (String)feature.getData(JCPFeatureFileValidator.class, SCRIPT_URL);
    }


    /*********************************
     * Private methods
     *********************************/


    private static String readGherkinScript(Object testObj, String featureFileLocation) {
        GherkinAssert.featureFileShouldBeFound(testObj, featureFileLocation);
        InputStream stream = testObj.getClass().getResourceAsStream(featureFileLocation);
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
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
        try {
            expectedFeature = parseGherkinScript(expectedScript);
        } catch (Exception ex) {
            throw new JCCannotContinueException(this.getClass().toString()+" error ", ex, GherkinAssert.ERROR_TYPES.LEXER_ERROR);
        }

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

        GherkinAssert.featureTitleShouldBeAsInDefinition(progress.getCurrentFeature(), expectedFeature);
    }

    protected void onFeatureEnd() {
        progress.getCurrentFeature().setData(this.getClass(), EXPECTED_TO_ACTUAL_SCENARIO_MAP, expectedScenarioToActualMap);
        progress.getCurrentFeature().setData(this.getClass(), EXPECTED_TO_ACTUAL_STEP_MAP, expectedStepToActualMap);
        GherkinAssert.sameNumberOfScenarios(expectedFeature, progress, expectedScenarioToActualMap);
    }
//
//    @Override
//    protected void onBackgroundStart() {
//        GherkinAssert.backgroundShouldNotHaveBeenAttachedIfNotDefined(expectedFeature, expectedScript);
//
//        // background indexes of actual and expected are equal (design decision)
//        int indexOfCurrentBackground = progress.getCurrentFeature().backgrounds.size()-1;
//        GherkinBackground expectedBackground = expectedFeature.backgrounds.get(indexOfCurrentBackground);
//
//        GherkinAssert.backgroundTitleShouldBeAsInDefinition(progress.getLatestBackground(), expectedBackground);
//        expectedScenario = expectedBackground;
//        expectedStep = null;
//    }
//
//    @Override
//    protected void onBackgroundEnd() {
//        boolean noMeaningfulExceptions =
//                progress.getCurrentScenario().getFatalExceptions().size()==0 &&
//                progress.getCurrentScenario().getTestExceptions().size()==0;
//
//        // if there were exceptions - no point checking scenario, as it most probably didn't run.
//        if (noMeaningfulExceptions) {
//            GherkinAssert.backgroundStepsNumberShouldBeAsDefined(expectedScenario, progress.getLatestBackground());
//        }
//        expectedScenario = null;
//    }

    protected void onScenarioStart() {
        GherkinFeature currentFeature = progress.getCurrentFeature();
        GherkinScenario currentScenario = progress.getCurrentScenario();

        expectedScenario = GherkinAssert.featureShouldContainTheFollowingScenario(expectedFeature, currentScenario);
        expectedScenarioToActualMap.put(expectedScenario, currentScenario);

//        if (currentFeature.isBackgroundDefined()) {
//
//            //todo: assert if we have an index issue
//
//            // find matching expected background and attach it to expected scenario
//            GherkinBackground expectedBackground = currentFeature.findBackgroundwithSameIndexFrom(expectedFeature, currentScenario.getBackground());
//            expectedBackground.parentScenario = expectedScenario;
//            expectedScenario.attachBackground(expectedBackground);
//        }

        expectedStep = null;
    }

    protected void onScenarioEnd() {
        boolean noMeaningfulExceptions =
                progress.getCurrentScenario().getFatalExceptions().size()==0 &&
                progress.getCurrentScenario().getTestExceptions().size()==0;

        // if there were exceptions - no point checking scenario, as it most probably didn't run.
        if (noMeaningfulExceptions) {
            GherkinAssert.scenarioStepsNumberShouldBeAsDefined(expectedScenario, progress.getCurrentScenario());
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
        GherkinAssert.nextStepTitleIsAsExpected(progress, expectedScenario, expectedStep);
        GherkinAssert.nextStepTypeIsAsExpected(progress, expectedScenario, expectedStep);

        // update link if everything is ok
        this.expectedStep = expectedStep;
    }

    protected void onStepEnd() {

    }


    protected void onStepFailure(Throwable ex) {

    }
}
