package com.hpe.jc.plugins;

import com.hpe.jc.errors.GherkinAssert;
import com.hpe.jc.gherkin.GherkinFeature;
import com.hpe.jc.gherkin.GherkinProgress;
import com.hpe.jc.gherkin.GherkinScenario;
import com.hpe.jc.gherkin.GherkinStep;
import gherkin.lexer.En;
import gherkin.lexer.Lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by koreny on 3/31/2017.
 */
public class JCPValidateFlowBy extends JCPlugin {

    // contains the feature file data (taken from the @feature annotation)
    protected GherkinFeature featureFile;

    // used to link to expected scenario and step from feature definition. Updated in start of scenario / step.
    private GherkinScenario linkToScenarioDefinition;
    private GherkinStep linkToStepDefinition;

    HashMap<GherkinScenario, GherkinScenario> file2actual = new HashMap<GherkinScenario, GherkinScenario>();

    protected JCPValidateFlowBy() {

    }

    public JCPValidateFlowBy(String featureFileLocation) {

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

    protected static GherkinFeature parseGherkinScript(String script) {
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
        boolean noMeaningfulExceptions =
                progress.getCurrentScenario().getFatalExceptions().size()==0 &&
                progress.getCurrentScenario().getTestExceptions().size()==0;

        // if there were exceptions - no point checking scenario, as it most probably didn't run.
        if (noMeaningfulExceptions) {
            GherkinAssert.ScenarioHasAllSteps(linkToScenarioDefinition, progress.getCurrentScenario());
        }
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
