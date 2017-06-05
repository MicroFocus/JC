package com.hpe.jc.errors;

import com.hpe.jc.gherkin.GherkinBackground;
import com.hpe.jc.gherkin.GherkinFeature;
import com.hpe.jc.GherkinProgress;
import com.hpe.jc.gherkin.GherkinScenario;
import com.hpe.jc.gherkin.GherkinStep;
import com.hpe.jc.JCCannotContinueException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koreny on 3/25/2017.
 */
public class GherkinAssert {



    public enum ERROR_TYPES {
        FEATURES_MISMATCH,
        SCENARIO_TOO_FEW,
        SCENARIO_MISMATCH,
        SCENARIO_NULL,
        STEP_TOO_MANY,
        STEP_TOO_FEW,
        STEP_TOO_FEW_IN_BACKGROUND,
        STEP_MISMATCH,
        BACKGROUND_MISSING,
        BACKGROUND_IS_NOT_EXPECTED,
        BACKGROUND_TITLE_MISMATCH,
    }

    public static void backgroundTitleShouldBeAsInDefinition(GherkinBackground latestBackground, GherkinBackground expectedbackground) {
        if (notTheSame(latestBackground.getDescription(), expectedbackground.getDescription())) {
            String message = String.format(
                    "Your background title is not as defined in the feature file\n" +
                            "Your background title is: \"%s\"\n" +
                            "The background defined in the feature file is \"%s\"\n",
                    latestBackground.getDescription(),
                    expectedbackground.getDescription());

            throw createException(message, ERROR_TYPES.BACKGROUND_TITLE_MISMATCH);
        }
    }

    public static void backgroundShouldHaveBeenAttachedIfDefined(GherkinScenario currentScenario, GherkinScenario expectedScenario) {
        if (expectedScenario.getBackground()!=null && currentScenario.getBackground()==null) {
            String message = String.format(
                            "You have not used a background in your test code, but your feature file have a background defined.\n" +
                            "Your background should look like this: \n%s" +
                            "You can copy the following code to your \'before test\' method: %s",
                    expectedScenario.getBackground().printScenario(),
                    expectedScenario.getBackground().printScenarioCode());

            throw createException(message, ERROR_TYPES.BACKGROUND_MISSING);

        }
    }

    public static void backgroundShouldNotHaveBeenAttachedIfNotDefined(GherkinFeature expectedFeature, String script) {
        if (expectedFeature.background == null) {
            String message = String.format(
                    "You have used a background in your test code, but your feature file does not have a background defined.\n" +
                    "Here is your feature file which does not contain a background definition:\n %s",
                    script);
            throw createException(message, ERROR_TYPES.BACKGROUND_IS_NOT_EXPECTED);
        }
    }

    public static void featureTitleShouldBeAsInDefinition(GherkinFeature actual, GherkinFeature expected) {
        if (notTheSame(actual.getDescription(), expected.getDescription())) {
            String message = String.format("\n" +
                    "feature description is not the same as the feature definition file specified. \n" +
                    "Actual: \"%s\" \n" +
                    "Expected: \"%s\" \n", actual.getDescription(), expected.getDescription());
            throw createException(message, ERROR_TYPES.FEATURES_MISMATCH);
        }
    }

    public static void backgroundStepsNumberShouldBeAsDefined(GherkinBackground expectedBackground, GherkinBackground currentBackground) {
        int dif = expectedBackground.steps.size() - currentBackground.steps.size();
        if (dif != 0) {
            String message = String.format(
                    "You seems to have missing %s steps that are defined in your background definition: \n" +
                            "your background should be: \n\n%s\n" +
                            "But it looks like that: \n\n%s\n",
                    String.valueOf(dif),
                    expectedBackground.
                            printScenario(),
                    currentBackground.
                            clone(new GherkinStep("X", "")).
                            printScenario(String.format("MISSING %d STEPS", dif)));

            throw createException(message, ERROR_TYPES.STEP_TOO_FEW_IN_BACKGROUND);
        }
    }

    public static void scenarioStepsNumberShouldBeAsDefined(GherkinScenario expectedScenario, GherkinScenario currentScenario) {
        int dif = expectedScenario.steps.size() - currentScenario.steps.size();
        if (dif != 0) {
            String message = String.format(
                    "You seems to have missing %s steps that are found on the scenario: \n" +
                            "your scenario should be: \n\n%s\n" +
                            "But it looks like that: \n\n%s\n",
                    String.valueOf(dif),
                    expectedScenario.printScenario(),
                    currentScenario.clone(new GherkinStep("X", "")).printScenario("MISSING STEPS"));

            throw createException(message, ERROR_TYPES.STEP_TOO_FEW);
        }
    }

    public static GherkinScenario featureShouldContainTheFollowingScenario(GherkinFeature featureDefinition, GherkinScenario actualScenario) {
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
                    "Your scenario description does not match any of the steps on the feature file\n" +
                    "This is your scenario: \n%s\n\n" +
                    "These are all the steps found in the feature file:\n\n", actualScenario.printScenarioTitle());
            for (GherkinScenario scenario : featureDefinition.scenarios) {
                message += scenario.printScenarioTitle() + "\n";
            }
            throw createException(message, ERROR_TYPES.SCENARIO_MISMATCH);
        }

        return result;
    }

    public static void currentScenarioIsValid(GherkinScenario currentScenario) {
        if (currentScenario == null) {
            throw createException(ERROR_TYPES.SCENARIO_NULL);
        }
    }

    public static void validExpectedNextStep(GherkinProgress progress, GherkinScenario linkToScenarioDefinition, GherkinStep expectedNextStep) {
        if (expectedNextStep == null) {
            String message = String.format(
                    "You seems to have an extra step that is not found on the feature file: \n" +
                            "your scenario should be: \n\n%s\n" +
                            "But it looks like that: \n\n%s\n",
                    linkToScenarioDefinition.printScenario(),
                    progress.getCurrentScenario().clone(progress.getCurrentStep()).printScenario( "EXTRA STEP"));
            throw createException(message, ERROR_TYPES.STEP_TOO_MANY);
        }
    }

    public static void nextStepTitleIsAsExpected(GherkinProgress progress, GherkinScenario linkToScenarioDefinition, GherkinStep expectedNextStep) {
        if (notTheSame(progress.getCurrentStep().getDescription(), expectedNextStep.getDescription())) {
            String message = String.format("\n" +
                    "We found a mismatch in one of your steps title: \n" +
                    "Actual: \n\n%s\n" +
                    "Expected: \n\n%s\n",
                    progress.getCurrentScenario().clone(progress.getCurrentStep()).printScenario( "WRONG STEP TITLE"),
                    linkToScenarioDefinition.printScenario());
            throw createException(message, ERROR_TYPES.STEP_MISMATCH);
        }
    }

    public static void nextStepTypeIsAsExpected(GherkinProgress progress, GherkinScenario linkToScenarioDefinition, GherkinStep expectedNextStep) {
        if (notTheSame(progress.getCurrentStep().type, expectedNextStep.type)) {
            String message = String.format("\n" +
                "We found a mismatch in one of your steps type: \n" +
                "Actual: \n\n%s\n" +
                "Expected: \n\n%s\n",
                progress.getCurrentScenario().clone(progress.getCurrentStep()).printScenario( "WRONG STEP TYPE"),
                linkToScenarioDefinition.printScenario());
            throw createException(message, ERROR_TYPES.STEP_MISMATCH);
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

    private static JCCannotContinueException createException(String message, ERROR_TYPES id) {
        return new JCCannotContinueException(message, id);
    }

    private static JCCannotContinueException createException(ERROR_TYPES id) {
        return new JCCannotContinueException("", id);
    }

    public static void sameNumberOfScenarios(GherkinFeature featureFile, GherkinProgress progress, HashMap<GherkinScenario, GherkinScenario> file2actual) {

        // let's fill this with forgotten steps...
        ArrayList<GherkinScenario> scenariosToImplement = new ArrayList<>();

        // #scenarios on feature file = #actual scenarios + #failed backgrounds
        int scenarioDif = featureFile.scenarios.size() - progress.getCurrentFeature().scenarios.size() - progress.getCurrentFeature().orphanBackgrounds.size();

        // forgot to implement some steps from within the feature definition
        if (scenarioDif > 0) {
            if (progress.getCurrentFeature().orphanBackgrounds.size()>0) {
                // wierd edge case where user forgot to implement a scenario and also background failed
                // so I have no idea which is the missing scenario vs. the scenario with the failed background.
                // decided to not do anything since user will get an error of his failed background, when he fixes the error, he will get this one...
                return;
            }
            String message = String.format("\nYou forgot to implement %s scenarios:\n", String.valueOf(scenarioDif));
            for (GherkinScenario scenarioDef : featureFile.scenarios) {
                if (!file2actual.containsKey(scenarioDef)) {
                    scenariosToImplement.add(scenarioDef);
                }
            }

            for (GherkinScenario scenario : scenariosToImplement) {
                message+= scenario.printScenario();
                message+="\n";
            }

            message += "\nimplement missing steps using below code:\n\n";

            for (GherkinScenario scenario : scenariosToImplement) {
                message+= scenario.printScenarioCode();
                message+="\n\n";
            }

            throw createException(message, ERROR_TYPES.SCENARIO_TOO_FEW);
        }
    }
}
