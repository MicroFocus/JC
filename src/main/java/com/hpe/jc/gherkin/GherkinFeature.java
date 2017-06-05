package com.hpe.jc.gherkin;

import java.util.ArrayList;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinFeature extends GherkinBaseEntity {

    public ArrayList<GherkinScenario> scenarios = new ArrayList<GherkinScenario>();
    public ArrayList<String> tags = new ArrayList<>();
    public GherkinBackground background = null;

    // used to store backgrounds that threw exception and their scenario never started to run...
    public ArrayList<GherkinBackground> orphanBackgrounds = new ArrayList<>();


    public GherkinFeature(String featureDescription) {
        super(featureDescription);
    }

    public String printGherkin() {
        String result = String.format("Feature: %s\n\n", getDescription());
        for (GherkinScenario scenario : scenarios) {
            result += String.format("%s\n\n", scenario.printGherkin());
        }
        return result;
    }
}
