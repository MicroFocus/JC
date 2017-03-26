package com.hpe;

import gherkin.lexer.Listener;
import java.util.List;

/**
 * Created by koreny on 3/24/2017.
 */
public class GherkinLexerListener implements Listener {

    public GherkinFeature currentFeature;
    GherkinScenario currentScenario;
    GherkinStep currentStep;

    public void comment(String s, Integer integer) {
    }

    public void tag(String s, Integer integer) {
    }

    public void feature(String s, String s1, String s2, Integer integer) {

        currentFeature = new GherkinFeature(s1);
    }

    public void background(String s, String s1, String s2, Integer integer) {
    }

    public void scenario(String s, String s1, String s2, Integer integer) {
        GherkinScenario scenario = new GherkinScenario(s1);

        currentFeature.scenarios.add(scenario);
        currentScenario = scenario;

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
