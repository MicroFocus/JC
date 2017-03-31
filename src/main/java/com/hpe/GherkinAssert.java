package com.hpe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koreny on 3/25/2017.
 */
public class GherkinAssert {

    public static void SameFeature(GherkinFeature actual, GherkinFeature expected) {
        if (notTheSame(actual.getDescription(), expected.getDescription())) {
            String message = String.format("" +
                    "feature description is not the same as the feature definition file specified. \n" +
                    "Your feature: \"%s\" \n" +
                    "Defined feature: \"%s\" \n", actual.getDescription(), expected.getDescription());
            throw createException(message);
        }
    }

    public static void SameScenario(GherkinScenario actual, GherkinScenario expected) {
        if (notTheSame(actual.getDescription(), expected.getDescription())) {
            throw createException();
        }
    }

    public static void SameStep(GherkinStep actual, GherkinStep expected) {
        if (notTheSame(actual.type, expected.type)) {
            throw createException();
        }

        if (notTheSame(actual.getDescription(), expected.getDescription())) {
            throw createException();
        }

    }

    public static void ScenarioHasAllSteps(GherkinScenario linkToScenarioDefinition, GherkinScenario currentScenario) {
        int dif = linkToScenarioDefinition.steps.size() - currentScenario.steps.size();
        if (dif != 0) {
            String message = String.format(
                    "You seems to have missing %s steps that are found on the feature file: \n" +
                            "your scenario should be: \n\n%s\n" +
                            "But it looks like that: \n\n%s\n",
                    String.valueOf(dif),
                    linkToScenarioDefinition.printScenario(),
                    currentScenario.clone(new GherkinStep("X", "")).printScenario("MISSING STEPS"));

            throw createException(message);
        }
    }

    public static GherkinScenario featureContainsScenario(GherkinFeature featureDefinition, GherkinScenario actualScenario) {
        // find scenario
        GherkinScenario result = null;
        for (GherkinScenario scenario : featureDefinition.scenarios) {
            if (theSame(actualScenario.getDescription(), scenario.getDescription())) {
                result = scenario;
                break;
            }
        }

        // error if no find
        if (result == null) {
            String message = String.format(
                    "Your scenario description does not match any of the scenarios on the feature file\n" +
                    "This is your scenario: \n%s\n\n" +
                    "These are all the scenarios found in the feature file:\n\n", actualScenario.printScenarioTitle());
            for (GherkinScenario scenario : featureDefinition.scenarios) {
                message += scenario.printScenarioTitle() + "\n";
            }
            throw createException(message);
        }

        return result;
    }

    public static void currentScenarioIsValid(GherkinScenario currentScenario) {
        if (currentScenario == null) {
            throw createException();
        }
    }

    public static void validExpectedNextStep(GherkinProgress progress, GherkinScenario linkToScenarioDefinition, GherkinStep expectedNextStep) {
        if (expectedNextStep == null) {
            String message = String.format(
                    "You seems to have an extra step that is not found on the feature file: \n" +
                            "your scenario should be: \n\n%s\n" +
                            "But it looks like that: \n\n%s\n",
                    linkToScenarioDefinition.printScenario(),
                    progress.getCurrentScenario().clone(progress.getCurrentStep()).printScenario(), "EXTRA STEP");
            throw createException(message);
        }
    }

    public static void nextStepIsAsExpected(GherkinProgress progress, GherkinScenario linkToScenarioDefinition, GherkinStep expectedNextStep) {
        if (notTheSame(progress.getCurrentStep().type, expectedNextStep.type)) {
            String message = String.format(
                    "The type of the step is not identical to the expected type: \n" +
                    "your scenario should be: \n\n%s\n" +
                    "But it looks like that: \n\n%s\n",
                    linkToScenarioDefinition.printScenario(),
                    progress.getCurrentScenario().printScenario(), "WRONG STEP TYPE");
            throw createException(message);
        }

        if (notTheSame(progress.getCurrentStep().getDescription(), expectedNextStep.getDescription())) {
            String message = String.format(
                    "The description of the step is not identical to the expected description: \n" +
                    "your scenario should be: \n\n%s\n" +
                    "But it looks like that: \n\n%s\n",
                    linkToScenarioDefinition.printScenario(),
                    progress.getCurrentScenario().clone(progress.getCurrentStep()).printScenario(), "WRONG DESCRIPTION");
            throw createException(message);
        }
    }


    public static JCException createClearExceptionFrom(Throwable orig, GherkinProgress progress) {
        String ex =
                "Flow of events:\n" +
                progress.getCurrentScenario().printScenario() +
                "<<<< BOOM >>>>\n" +
                orig.getMessage();

        throw new JCException(ex, orig);
    }

    private static boolean notTheSame(String str1, String str2) {
        return str1.trim().compareToIgnoreCase(str2.trim())!=0;
    }

    private static boolean theSame(String str1, String str2) {
        return !notTheSame(str1, str2);
    }

    private static JCCannotContinueException createException(String message) {
        return new JCCannotContinueException(message);
    }

    private static JCCannotContinueException createException() {
        return new JCCannotContinueException();
    }

    public static void sameNumberOfScenarios(GherkinFeature featureFile, GherkinProgress progress, HashMap<GherkinScenario, GherkinScenario> file2actual) {

        // let's fill this with forgotten scenarios...
        ArrayList<GherkinScenario> scenariosToImplement = new ArrayList<>();

        int scenarioDif = featureFile.scenarios.size() - progress.getCurrentFeature().scenarios.size();
        // forgot to implement some scenarios from within the feature definition
        if (scenarioDif > 0) {
            String message = String.format("You forgot to implement %s scenarios:\n", String.valueOf(scenarioDif));
            for (GherkinScenario scenarioDef : featureFile.scenarios) {
                if (!file2actual.containsKey(scenarioDef)) {
                    scenariosToImplement.add(scenarioDef);
                }
            }

            for (GherkinScenario scenario : scenariosToImplement) {
                message+= scenario.printScenario();
                message+="\n";
            }

            message += "\nimplement missing scenarios using below code:\n\n";

            for (GherkinScenario scenario : scenariosToImplement) {
                message+= scenario.printScenarioCode();
                message+="\n\n";
            }

            throw createException(message);
        }
    }
}
