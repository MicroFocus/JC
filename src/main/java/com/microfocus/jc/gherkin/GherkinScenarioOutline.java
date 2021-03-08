package com.microfocus.jc.gherkin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koreny on 6/9/2017.
 */
public class GherkinScenarioOutline extends GherkinScenario {

    public List<List<String>> paramValues = new ArrayList<>();
    public List<String> paramKeys = new ArrayList<>();

    public GherkinScenarioOutline(String description) {
        super(description);

    }
}
