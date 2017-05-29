package com.hpe.jc.gherkin;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinStep extends GherkinBaseEntity {
    public String type;

    public GherkinScenario parent;

    public GherkinStep(String type, String description) {
        super(description);
        this.type = type;
    }

    public String printStep() {
        return String.format("|    %s %s", type.trim(), getDescription().trim());
    }

    @Override
    public String printGherkin() {
        return String.format("%s %s", type, getDescription());
    }
}