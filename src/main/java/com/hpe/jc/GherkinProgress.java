package com.hpe.jc;

import com.hpe.jc.errors.GherkinAssert;
import com.hpe.jc.gherkin.*;

/**
 * Created by koreny on 3/21/2017.
 */
public class GherkinProgress {

    public GherkinProgress(Object test) {
        this.test = test;
        pluginManager.setProgress(this);
    }

    public GherkinProgress(Object test, JCPlugin[] plugins) {
        this(test);

        pluginManager.registerPlugins(this, plugins);
    }

    // save tested class (for possibly reporting the name of the class only, no real need of it)
    private Object test;

    // track current feature/scenario/step
    private GherkinBackground latestBackground;
    private GherkinFeature currentFeature;
    private GherkinScenario currentScenario;
    private GherkinStep currentStep;
    private GherkinBaseEntity current;

    // execute the plugins. Funny that it is a plugin itself...
    private PluginManager pluginManager = new PluginManager();

    /*************************************
     * Public methods
     *************************************/

    public Object getTestObject() {
        return test;
    }

    public GherkinBackground getLatestBackground() { return latestBackground; }

    public GherkinFeature getCurrentFeature() { return currentFeature; }

    public GherkinScenario getCurrentScenario() {
        return currentScenario;
    }

    public GherkinStep getCurrentStep() { return currentStep; }

    public GherkinBaseEntity getCurrent() { return current; }

    public boolean isCurrentScenarioHasException() {
        return currentScenario.getTestExceptions().size()>0;
    }

    /*************************************
     * Public methods - update progress
     *************************************/

    public void updateFeature(GherkinFeature actualFeature) {
        // end of feature
        if (actualFeature == null) {
            current = currentFeature;
            pluginManager.onFeatureEnd();
            current = currentFeature = null;
        } else {
            // start of feature
            current = currentFeature = actualFeature;
            pluginManager.onFeatureStart();
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
                pluginManager.onStepEnd();
                current = currentStep.parent;
                currentStep = null;
            }
            pluginManager.onScenarioEnd();

            //current = currentScenario.getParent();
            //currentScenario = null;

        } else {

            // signal start of new scenario
            boolean backgroundJustFinished = currentScenario instanceof GherkinBackground;
            if (backgroundJustFinished) {
                GherkinBackground latestBackground = (GherkinBackground) currentScenario;
                actualScenario.attachBackground(latestBackground);
            }
            actualScenario.setParent(currentFeature);
            current = currentScenario = actualScenario;
            pluginManager.onScenarioStart();
            currentFeature.scenarios.add(currentScenario);
        }
    }


    public void updateBackground(GherkinBackground background) {
        // nothing to do if no scenario started, and reporting on end of scenario...
        if (background == null && currentScenario == null) {
            return;
        }

        // signal the end of current background
        if (background == null) {
            // end last step if there is one
            if (currentStep!=null) {
                pluginManager.onStepEnd();
                current = currentStep.parent;
                currentStep = null;
            }
            pluginManager.onBackgroundEnd();
            //current = currentFeature;
            //currentScenario = null;

        } else {

            // signal start of background
            //background.setParent(currentFeature);
            //currentFeature.background = background;
            current = currentScenario = latestBackground = background;
            pluginManager.onBackgroundStart();
        }

    }


    public void updateStep(GherkinStep nextStep) {
        // not first step in the scenario -> let's close the previous step
        if (currentStep != null) {
            current = currentStep;
            pluginManager.onStepEnd();
        }
        // this is null when exception is thrown in step and need to end it without open another one...
        if (nextStep != null) {
            nextStep.parent = currentScenario;
            current = currentStep = nextStep;
            pluginManager.onStepStart();
            currentScenario.steps.add(currentStep);
            GherkinAssert.currentScenarioIsValid(currentScenario);
        }
    }


    public void updateException(Throwable ex) {
        pluginManager.onStepFailure(ex);
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
