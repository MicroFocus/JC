package com.hpe;

import sun.awt.windows.ThemeReader;

import java.util.ArrayList;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinFeature extends GherkinBaseEntity {

    public ArrayList<GherkinScenario> scenarios = new ArrayList<GherkinScenario>();

    public GherkinFeature(String featureDescription) {
        super(featureDescription);
    }


}
