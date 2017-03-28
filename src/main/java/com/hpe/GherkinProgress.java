package com.hpe;

/**
 * Created by koreny on 3/21/2017.
 */
public class GherkinProgress {

    public GherkinProgress(Object test) {
        this.test = test;
        check = new GherkinAssert(this);
    }

    // utility to check and compare gherkin related stuff, and created sophisticated exceptions
    private GherkinAssert check;

    // save tested class (for possibly reporting the name of the class only, no real need of it)
    private Object test;

    // contains the feature file data (taken from the @feature annotation)
    private GherkinFeature featureDefinition;

    // used to link to expected scenario and step from feature definition. Updated in start of scenario / step.
    private GherkinScenario linkToScenarioDefinition;
    private GherkinStep linkToStepDefinition;

    // track actual feature
    private GherkinFeature currentFeature;
    private GherkinScenario currentScenario;
    private GherkinStep currentStep;

    /*************************************
     * Private methods
     *************************************/

    private boolean isFeatureFileDefined() {
        return featureDefinition != null;
    }

    /*************************************
     * Public methods
     *************************************/

    public GherkinFeature getCurrentFeature() { return currentFeature; }

    public GherkinScenario getCurrentScenario() {
        return currentScenario;
    }

    public GherkinScenario getScenarioDefinition() {
        return linkToScenarioDefinition;
    }

    public boolean isCurrentScenarioHasException() {
        return currentScenario.exception!=null;
    }

    public void setFeatureDefinition(GherkinFeature feature) {
        featureDefinition = feature;
    }

    public GherkinFeature getFeatureDefinition() {
        return featureDefinition;
    }

    /*************************************
     * Public methods - update progress
     *************************************/

    public void updateFeature(GherkinFeature actualFeature) {
            // end of feature
        if (actualFeature == null) {
            check.sameNumberOfScenarios();

        } else {
            // start of feature
            currentFeature = actualFeature;
            check.SameFeature(featureDefinition, currentFeature);
        }
    }

    public void updateScenario(GherkinScenario actualScenario) {

        // nothing to do if no scenario started, and reporting on end of scenario...
        if (actualScenario == null && currentScenario == null) {
            return;
        }

        // signal the end of current scenario
        if (actualScenario == null && currentScenario != null) {
            // if feature definition exists -> check # of steps
            if (isFeatureFileDefined()) {
                check.linkToScenarioInFeatureDefinitionIsValid(linkToScenarioDefinition, featureDefinition);
                check.ScenarioHasAllSteps(linkToScenarioDefinition, currentScenario);
            }
            linkToScenarioDefinition = null;
            currentScenario = null;
            return;
        }

        // signal start of new scenario
        // currentScenario might not be null if previous scenario throw an exception
        if (actualScenario != null) {
            if (isFeatureFileDefined()) {
                linkToScenarioDefinition = check.featureContainsScenario(featureDefinition, actualScenario);
                actualScenario.setLinktoScenarioDef(linkToScenarioDefinition);
                linkToStepDefinition = null;
            }
            currentScenario = actualScenario;
            currentFeature.scenarios.add(actualScenario);
            return;
        }
    }

    public void updateStep(GherkinStep nextStep) {
        check.currentScenarioIsValid(currentScenario);
        check.linkToScenarioInFeatureDefinitionIsValid(linkToScenarioDefinition, featureDefinition);

        if (isFeatureFileDefined()) {
            GherkinStep expectedNextStep = linkToScenarioDefinition.getNextStep(linkToStepDefinition);
            check.validExpectedNextStep(linkToScenarioDefinition, nextStep, expectedNextStep);
            check.nextStepIsAsExpected(linkToScenarioDefinition, expectedNextStep, currentScenario, nextStep);

            // update link if everything is ok
            linkToStepDefinition = expectedNextStep;
        }

        currentStep = nextStep;
        currentScenario.steps.add(nextStep);
    }

    public void updateException(Throwable e) {
        if (currentStep != null) {
            currentStep.exception = e;
        }

        if (currentScenario != null) {
            currentScenario.exception = e;
        }
    }

    public void report() {


    }


}
