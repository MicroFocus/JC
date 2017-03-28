package com.hpe;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinStep {
    public String type;
    public String description;
    public Throwable exception;

    public GherkinStep(String type, String description) {
        this.type = type;
        this.description = description;
    }


    public String printStep() {
        return String.format("|    %s %s", type.trim(), description.trim());
    }

}
