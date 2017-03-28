package com.hpe;

import java.util.ArrayList;

/**
 * Created by koreny on 3/20/2017.
 */
public class GherkinScenario {

    public Throwable exception;
    public ArrayList<GherkinStep> steps = new ArrayList<GherkinStep>();
    public String description;
    private GherkinScenario linktoScenarioDef;

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


    public String printScenarioTitle() {
        return String.format("|  Scenario: %s", description);
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
                "\t\tjc.scenario(\"%s\", ()->{\n", toCamelCase(description), description);
        for (GherkinStep step : steps) {
            code+= String.format(
                "\t\t\tjc.%s(\"%s\");\n", step.type.toLowerCase(), step.description);
        }
        code += String.format(
                "\t\t});\n" +
                "\t}");

        return code;
    }

    private static String toCamelCase(String s) {
        String result = "";
        for (String part : s.trim().split(" ")) {
            result += part.substring(0,1).toUpperCase() + part.substring(1).toLowerCase();
        }
        return result.substring(0,1).toLowerCase() + result.substring(1);
    }

    public GherkinScenario getLinktoScenarioDef() {
        return linktoScenarioDef;
    }

    public void setLinktoScenarioDef(GherkinScenario linktoScenarioDef) {
        this.linktoScenarioDef = linktoScenarioDef;
    }
}
