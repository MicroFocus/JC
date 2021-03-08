package com.hpe.jc.gherkin;

import java.util.ArrayList;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinFeature extends GherkinBaseEntity {

    public ArrayList<GherkinScenario> scenarios = new ArrayList<>();
    public ArrayList<GherkinBackground> backgrounds = new ArrayList<>();
    public ArrayList<String> tags = new ArrayList<>();

    public GherkinFeature(String featureDescription) {
        super(featureDescription);
    }

    public int getNumberOfFailedBackgrounds() {
        int counter = 0;
        for (GherkinBackground background : backgrounds) {
            if (background.hasMeaningfulExceptions()) {
                counter++;
            }
        }
        return counter;
    }

    public String printGherkin() {
        String result = String.format("Feature: %s\n\n", getDescription());
        for (GherkinScenario scenario : scenarios) {
            result += String.format("%s\n\n", scenario.printGherkin());
        }
        return result;
    }
}
