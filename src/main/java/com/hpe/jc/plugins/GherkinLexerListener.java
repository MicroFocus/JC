package com.hpe.jc.plugins;

import com.hpe.jc.gherkin.*;
import gherkin.lexer.Listener;
import java.util.List;

/**
 * Created by koreny on 3/24/2017.
 *
 * This is a lexer that creates the expected feature structure from the feature definition file.
 * The expected feature will be compared to the actual feature structure by the JCPFeatureFileValidator plugin so we can point out differences
 * Structure is:
 * - feature
 *   + Scenarios
 *     - background Steps
 *     - regular steps
 */
public class GherkinLexerListener implements Listener {

    public GherkinFeature currentFeature = new GherkinFeature("");
    GherkinBackground background = null;
    GherkinScenario currentScenario;
    GherkinStep currentStep;

    public void comment(String s, Integer integer) {
    }

    public void tag(String tagName, Integer line) {
        currentFeature.tags.add(tagName);
    }

    public void feature(String element, String title, String description, Integer line) {

        currentFeature.setDescription(title);
    }

    public void background(String element, String title, String description, Integer line) {
        currentScenario = background = new GherkinBackground(title);
    }

    public void scenario(String element, String title, String description, Integer integer) {
        GherkinScenario scenario = new GherkinScenario(title);

        setupScenario(scenario);
    }

    private void setupScenario(GherkinScenario scenario) {
        scenario.setParent(currentFeature);

        // Copy background steps to current scenario
        if (background != null) {
            for (GherkinStep step : background.steps) {
                scenario.steps.add(new GherkinStep(step, background));
            }
        }

        currentFeature.scenarios.add(scenario);
        currentScenario = scenario;
    }

    public void scenarioOutline(String element, String title, String s2, Integer line) {
        GherkinScenarioOutline scenario = new GherkinScenarioOutline(title);

        setupScenario(scenario);
    }

    public void examples(String element, String s1, String s2, Integer line) {
    }

    public void step(String element, String description, Integer integer) {
        GherkinStep step = new GherkinStep(element, description);

        currentScenario.steps.add(step);
        currentStep = step;
    }

    public void row(List<String> params, Integer line) {
        GherkinScenarioOutline scenario = (GherkinScenarioOutline)currentScenario;

        boolean firstRowOfTable = scenario.paramKeys.size()==0;
        if (firstRowOfTable) {
            scenario.paramKeys = params;
        } else {
            scenario.paramValues.add(params);
        }
    }

    public void docString(String s, String s1, Integer integer) {
    }

    public void eof() {
    }
}
