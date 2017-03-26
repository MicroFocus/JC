package com.hpe;

/**
 * Created by koreny on 3/25/2017.
 */
public class GherkinAssert {

    GherkinProgress progress;

    public GherkinAssert(GherkinProgress progress) {
        this.progress = progress;
    }

    public void SameFeature(GherkinFeature actual, GherkinFeature expected) {
        if (notTheSame(actual.description, expected.description)) {
            String message = String.format("" +
                    "feature description is not the same the feature definition file specified. \n" +
                    "Your feature: \"%s\" \n" +
                    "Defined feature: \"%s\" \n", actual.description, expected.description);
            throw createException(message);
        }
    }

    public void SameScenario(GherkinScenario actual, GherkinScenario expected) {
        if (notTheSame(actual.description, expected.description)) {
            throw createException();
        }
    }

    public void SameStep(GherkinStep actual, GherkinStep expected) {
        if (notTheSame(actual.type, expected.type)) {
            throw createException();
        }

        if (notTheSame(actual.description, expected.description)) {
            throw createException();
        }

    }

    public void ScenarioHasAllSteps(GherkinScenario linkToScenarioDefinition, GherkinScenario currentScenario) {
        int dif = linkToScenarioDefinition.steps.size() - currentScenario.steps.size();
        if (dif != 0) {
            String message = String.format(
                    "You seems to have missing %s steps that are found on the feature file: \n" +
                            "your scenario should be: \n\n%s\n" +
                            "But it looks like that: \n\n%s\n",
                    String.valueOf(dif),
                    printScenario(linkToScenarioDefinition, "MISSING STEPS"),
                    printScenario(currentScenario));

            throw createException(message);
        }
    }

    public GherkinScenario featureContainsScenario(GherkinFeature featureDefinition, GherkinScenario actualScenario) {
        // find scenario
        GherkinScenario result = null;
        for (GherkinScenario scenario : featureDefinition.scenarios) {
            if (theSame(actualScenario.description, scenario.description)) {
                result = scenario;
                break;
            }
        }

        // error if no find
        if (result == null) {
            String message = String.format(
                    "Your scenario description does not match any of the scenarios on the feature file\n" +
                    "This is your scenario: \n%s\n\n" +
                    "These are all the scenarios found in the feature file:\n\n", printScenarioTitle(actualScenario));
            for (GherkinScenario scenario : featureDefinition.scenarios) {
                message += printScenarioTitle(scenario) + "\n";
            }
            throw createException(message);
        }

        return result;
    }

    public void currentScenarioIsValid(GherkinScenario currentScenario) {
        if (currentScenario == null) {
            throw createException();
        }
    }

    public void linkToScenarioInFeatureDefinitionIsValid(GherkinScenario linkToScenarioDefinition, GherkinFeature featureDefinition) {
        if (featureDefinition != null && linkToScenarioDefinition == null) {
            throw createException();
        }
    }

    public void validExpectedNextStep(GherkinScenario linkToScenarioDefinition, GherkinStep nextStep, GherkinStep expectedNextStep) {
        if (expectedNextStep == null) {
            String message = String.format(
                    "You seems to have an extra step that is not found on the feature file: \n" +
                            "your scenario should be: \n\n%s\n" +
                            "But it looks like that: \n\n%s\n",
                    printScenario(progress.getExpectedScenario()),
                    printScenario(progress.getCurrentScenario().clone(nextStep), "EXTRA STEP"));
            throw createException(message);
        }
    }

    public void nextStepIsAsExpected(GherkinScenario linkToScenarioDefinition, GherkinStep expectedNextStep, GherkinScenario currentScenario, GherkinStep nextStep) {
        if (notTheSame(nextStep.type, expectedNextStep.type)) {
            String message = String.format(
                    "The type of the step is not identical to the expected type: \n" +
                    "your scenario should be: \n\n%s\n" +
                    "But it looks like that: \n\n%s\n",
                    printScenario(progress.getExpectedScenario()),
                    printScenario(progress.getCurrentScenario(), "WRONG STEP TYPE"));
            throw createException(message);
        }

        if (notTheSame(nextStep.description, expectedNextStep.description)) {
            String message = String.format(
                    "The description of the step is not identical to the expected description: \n" +
                    "your scenario should be: \n\n%s\n" +
                    "But it looks like that: \n\n%s\n",
                    printScenario(progress.getExpectedScenario()),
                    printScenario(progress.getCurrentScenario(), "WRONG DESCRIPTION"));
            throw createException(message);
        }
    }

    private String printScenario(GherkinScenario scenario) {
        return printScenario(scenario, "");
    }

    private String printScenario(GherkinScenario scenario, String arrowText) {
        String result = printScenarioTitle(scenario);
        int stepsNum = scenario.steps.size();

        result+="\n";
        for (int i = 0; i<stepsNum - 1; i++) {
            result += printStep(scenario.steps.get(i));
            result += "\n";
        }
        if (stepsNum>0) {
           result += printStep(scenario.steps.get(stepsNum-1));
           if (!arrowText.isEmpty()) {
               result += String.format("   <================= %s", arrowText);
           }
           result += "\n";
        }
        return result;
    }

    private String printScenarioTitle(GherkinScenario scenario) {
        return String.format("|  Scenario: %s", scenario.description);
    }

    private String printStep(GherkinStep step) {
        return String.format("|    %s %s", step.type, step.description);
    }

    private boolean notTheSame(String str1, String str2) {
        return str1.trim().compareToIgnoreCase(str2.trim())!=0;
    }

    private boolean theSame(String str1, String str2) {
        return !notTheSame(str1, str2);
    }

    private GherkinException createException(String message) {
        return new GherkinException(message);
    }

    private GherkinException createException() {
        return new GherkinException();
    }

}
