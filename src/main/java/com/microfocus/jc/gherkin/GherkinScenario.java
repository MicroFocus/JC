package com.microfocus.jc.gherkin;

import java.util.ArrayList;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinScenario extends GherkinBaseEntity{

    private GherkinFeature parent;
    public ArrayList<GherkinStep> steps = new ArrayList<GherkinStep>();

    public void setParent(GherkinFeature parent) {
        this.parent = parent;
    }

    public GherkinScenario(String description) {

        super(description);
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
        GherkinScenario clone = clone();
        clone.steps.add(extraStep);

        return clone;
    }

    public GherkinScenario clone() {
        GherkinScenario clone = new GherkinScenario(getDescription());
        clone.steps = (ArrayList<GherkinStep>) steps.clone();

        return clone;
    }

    public String printScenarioTitle() {
        return String.format("|  Scenario: %s", getDescription());
    }

    public String printScenario() {
        return printScenario("");
    }

    public String printScenario(String arrowText) {
        String result = printScenarioTitle();
        int stepsNum = steps.size();

        result+="\n";
        for (int i = 0; i<stepsNum - 1; i++) {
            result += steps.get(i).printStep();
            result += "\n";
        }
        if (stepsNum>0) {
            result += steps.get(stepsNum-1).printStep();
            if (!arrowText.isEmpty()) {
                result += String.format("   <================= %s", arrowText);
            }
            result += "\n";
        }
        return result;
    }

    public String printScenarioCode()
    {
        String code = String.format(
                "\t@Test\n" +
                "\tpublic void %s() {\n"+
                "\t\tscenario(\"%s\", ()->{\n", toCamelCase(getDescription()), getDescription());
        for (GherkinStep step : steps) {
            code+= String.format(
                "\t\t\t%s(\"%s\");\n", step.type.toLowerCase(), step.getDescription());
        }
        code += String.format(
                "\t\t});\n" +
                "\t}");

        return code;
    }

    protected static String toCamelCase(String s) {
        String result = "";
        for (String part : s.trim().split(" ")) {
            result += part.substring(0,1).toUpperCase() + part.substring(1).toLowerCase();
        }
        return result.substring(0,1).toLowerCase() + result.substring(1);
    }

    @Override
    public String printGherkin() {
        String result = String.format("Scenario: %s\n", getDescription());
        for (GherkinStep step : steps) {
            result += String.format("%s\n", step.printGherkin());
        }
        return result;
    }

}
