package com.hpe;

import java.util.ArrayList;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinFeature {

    public ArrayList<GherkinScenario> scenarios = new ArrayList<GherkinScenario>();
    public String description;

    public GherkinFeature(String featureDescription) {
        description = featureDescription;
    }
}
