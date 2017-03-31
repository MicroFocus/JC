package com.hpe;

import java.util.ArrayList;

/**
 * Created by koreny on 3/21/2017.
 */
public class GherkinProgress {

    public GherkinProgress(Object test) {
        this.test = test;
    }

    public GherkinProgress(Object test, JCPlugin[] plugins) {
        this(test);

        for (JCPlugin plugin : plugins) {
            pluginManager.registerPlugin(plugin);
        }
    }

    // save tested class (for possibly reporting the name of the class only, no real need of it)
    private Object test;

    // track current feature/scenario/step
    private GherkinFeature currentFeature;
    private GherkinScenario currentScenario;
    private GherkinStep currentStep;

    // execute the plugins. Funny that it is a plugin itself...
    private PluginManager pluginManager = new PluginManager();

    /*************************************
     * Public methods
     *************************************/

    public GherkinFeature getCurrentFeature() { return currentFeature; }

    public GherkinScenario getCurrentScenario() {
        return currentScenario;
    }

    public GherkinStep getCurrentStep() { return currentStep; }

    public boolean isCurrentScenarioHasException() {
        return currentScenario.getTestExceptions().size()>0;
    }

    /*************************************
     * Public methods - update progress
     *************************************/

    public void updateFeature(GherkinFeature actualFeature) {
            // end of feature
        if (actualFeature == null) {
            pluginManager.onFeatureEnd(this);
            currentFeature = actualFeature;

        } else {
            // start of feature
            currentFeature = actualFeature;
            pluginManager.onFeatureStart(this);

        }
    }

    public void updateScenario(GherkinScenario actualScenario) {

        // nothing to do if no scenario started, and reporting on end of scenario...
        if (actualScenario == null && currentScenario == null) {
            return;
        }

        // signal the end of current scenario
        if (actualScenario == null) {
            // end last step if there is one
            if (currentStep!=null) {
                pluginManager.onStepEnd(this);
                currentStep = null;
            }
            pluginManager.onScenarioEnd(this);
            // if feature definition exists -> check # of steps
            currentScenario = null;

        } else {

            // signal start of new scenario
            // currentScenario might not be null if previous scenario throw an exception
            currentScenario = actualScenario;
            pluginManager.onScenarioStart(this);
            currentFeature.scenarios.add(currentScenario);
        }
    }

    public void updateStep(GherkinStep nextStep) {
        // not first step in the scenario -> let's close the previous step
        if (currentStep != null) {
            pluginManager.onStepEnd(this);
        }
        currentStep = nextStep;
        pluginManager.onStepStart(this);
        currentScenario.steps.add(currentStep);
        GherkinAssert.currentScenarioIsValid(currentScenario);
    }

    public void updateException(Throwable ex) {
        pluginManager.onStepFailure(this, ex);
        if (currentStep != null) {
            currentStep.addTestException(ex);
        }

        if (currentScenario != null) {
            currentScenario.addTestException(ex);
        }
    }

    public void report() {

    }


}
