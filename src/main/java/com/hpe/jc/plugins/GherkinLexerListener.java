package com.hpe.jc.plugins;

import com.hpe.jc.gherkin.GherkinBackground;
import com.hpe.jc.gherkin.GherkinFeature;
import com.hpe.jc.gherkin.GherkinScenario;
import com.hpe.jc.gherkin.GherkinStep;
import gherkin.lexer.Listener;
import java.util.List;

/**
 * Created by koreny on 3/24/2017.
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
        currentScenario = currentFeature.background = background = new GherkinBackground(title);

    }

    public void scenario(String element, String title, String description, Integer integer) {
        GherkinScenario scenario = new GherkinScenario(title);

        currentFeature.scenarios.add(scenario);
        currentScenario = scenario;
        if (background != null) {
            currentScenario.attachBackground(background.clone());
        }

    }

    public void scenarioOutline(String s, String s1, String s2, Integer integer) {
    }

    public void examples(String s, String s1, String s2, Integer integer) {
    }

    public void step(String s, String s1, Integer integer) {
        GherkinStep step = new GherkinStep(s, s1);

        currentScenario.steps.add(step);
        currentStep = step;
    }

    public void row(List<String> list, Integer integer) {
    }

    public void docString(String s, String s1, Integer integer) {
    }

    public void eof() {
    }
}
