package com.hpe;

import java.util.ArrayList;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinScenario {

    public Throwable exception;
    public ArrayList<GherkinStep> steps = new ArrayList<GherkinStep>();
    public String description;

    public GherkinScenario(String description) {
        this.description = description;
    }

    public GherkinStep getNextStep(GherkinStep step) {

        // no step -> return first step
        if (step == null) {
            if (steps.size()>0) {
                return steps.get(0);
            } else {
                return null;
            }
        }

        int index = steps.indexOf(step);

        // step was not found
        if (index<0) {
            return null;
        }

        if (index+1 >= steps.size()) {
            // that wa the last step
            return null;
        } else {
            // return next step
            return steps.get(index+1);
        }
    }

    public GherkinScenario clone(GherkinStep extraStep) {
        GherkinScenario clone = new GherkinScenario(description);
        clone.steps = (ArrayList<GherkinStep>) steps.clone();
        clone.steps.add(extraStep);

        return clone;
    }

}
