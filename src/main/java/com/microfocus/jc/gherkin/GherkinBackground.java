package com.microfocus.jc.gherkin;

/**
 * Created by koreny on 3/20/2017.
 *
 *
 */
public class GherkinBackground extends GherkinScenario{

    public GherkinBackground(String description) {

        super(description);
    }

    public GherkinBackground clone(GherkinStep extraStep) {
        GherkinBackground clone = clone();
        clone.steps.add(extraStep);

        return clone;
    }

    public GherkinBackground clone() {
        GherkinBackground newBackground = new GherkinBackground(getDescription());

        for (GherkinStep step : steps) {
            newBackground.steps.add(new GherkinStep(step, newBackground));
        }

        return newBackground;
    }



    public String printScenarioTitle() {
        return String.format("|  Background: %s", getDescription());
    }

    public String printScenarioCode()
    {
        String code = String.format(
                "\t@Test\n" +
                "\tpublic void %s() {\n"+
                "\t\tbackground(\"%s\", ()->{\n", toCamelCase(getDescription()), getDescription());
        for (GherkinStep step : steps) {
            code+= String.format(
                "\t\t\t%s(\"%s\");\n", step.type.toLowerCase(), step.getDescription());
        }
        code += String.format(
                "\t\t});\n" +
                "\t}");

        return code;
    }


    @Override
    public String printGherkin() {
        String result = String.format("Background: %s\n", getDescription());
        for (GherkinStep step : steps) {
            result += String.format("%s\n", step.printGherkin());
        }
        return result;
    }
}
