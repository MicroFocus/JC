/**
 * Created by koreny on 3/20/2017.
 */

package com.hpe.jc;

import com.hpe.jc.errors.GherkinAssert;
import com.hpe.jc.gherkin.GherkinFeature;
import com.hpe.jc.gherkin.GherkinScenario;
import com.hpe.jc.gherkin.GherkinStep;
import com.hpe.jc.plugins.Feature;
import com.hpe.jc.plugins.FeatureFileAt;
import com.hpe.jc.plugins.JCPFeatureFileValidator;
import com.hpe.jc.plugins.JCPStepTimer;
import com.hpe.jc.plugins.OctaneFormatter.JCOctaneCucumberFormatter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;

public class JC {

    /*********************************
     * Private properties
     *********************************/

    // used to:
    // 1. track progress of test
    // 2. compare against real feature file
    private static HashMap<Class<?>, GherkinProgress> progressMap = new HashMap<>();

    // calling background will not actually run it.
    // the scenario will run the background before it begins.
    private static HashMap<Class<?>, Runnable> backgroundMap = new HashMap<>();

    private static GherkinProgress getProgress() {
        Class<?> currentTestClass = findCalledClassWithAnnotation(Feature.class);
        if (!progressMap.containsKey(currentTestClass)) {
            initializeInstance(currentTestClass, null);
        }

        return progressMap.get(currentTestClass);
    }

    public static void reset() {
        reset(null);
    }

    public static void reset(JCPlugin[] plugins) {
        progressMap.remove(findCalledClassWithAnnotation(Feature.class));
        backgroundMap.remove(findCalledClassWithAnnotation(Feature.class));
        Class<?> currentTestClass = findCalledClassWithAnnotation(Feature.class);
        initializeInstance(currentTestClass, plugins);
    }

    private static Runnable getBackground() {
        Class<?> currentTestClass = findCalledClassWithAnnotation(Feature.class);
        if (!backgroundMap.containsKey(currentTestClass)) {
            return null;
        }

        return backgroundMap.get(currentTestClass);
    }



    public static void addPlugin(JCPlugin plugin) {
        getProgress().registerPlugins(new JCPlugin[] {plugin});
    }

    /*********************************
     * Constructors methods - JC definitions
     *********************************/

    private static void initializeInstance(Class<?> currentTestClass, JCPlugin[] addedPlugins) {
        // create new gherkin progress instance
        progressMap.put(currentTestClass, new GherkinProgress(currentTestClass));

        // get feature definition
        Feature featureAnnotation = currentTestClass.getAnnotation(Feature.class);
        GherkinAssert.featureAnnotationNotFound(featureAnnotation, currentTestClass);

        // init plugins
        ArrayList<JCPlugin> plugins = new ArrayList<>();
        plugins.add(new JCPStepTimer());

        // add feature file validation plugins if exist
        FeatureFileAt featureFileAt = currentTestClass.getAnnotation(FeatureFileAt.class);
        if (featureFileAt != null) {
            plugins.add(new JCPFeatureFileValidator(featureFileAt.value()));
            plugins.add(new JCOctaneCucumberFormatter());
        }

        // register plugins
        getProgress().registerPlugins(plugins.toArray(new JCPlugin[plugins.size()]));
        if (addedPlugins!=null) {
            getProgress().registerPlugins(addedPlugins);
        }

        // update the progress with the feature
        GherkinFeature feature = new GherkinFeature(featureAnnotation.value());
        getProgress().updateFeature(feature);

    }

    // Using this constructor will compare between feature file and actual steps and steps
    public JC(Object test, JCPlugin[] plugins, String featureDescription) {
        getProgress().registerPlugins(plugins);

        // update the progress with the feature
        GherkinFeature feature = new GherkinFeature(featureDescription);
        getProgress().updateFeature(feature);
    }


    // Using this constructor will only create a report of what happened
    public JC(Object test, String featureDescription) {
        // update the progress with the feature
        GherkinFeature actualFeature = new GherkinFeature(featureDescription);
        getProgress().updateFeature(actualFeature);
    }

    /*********************************
     * Utils for better integration with 3rd party libraries
     *********************************/

    public static Class<?> findCalledClassWithAnnotation(Class<? extends Annotation> targetAnnotation) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        Class<?> cls = null;
        Annotation result = null;

        for (StackTraceElement element : stack) {
            try {
                cls = Class.forName(element.getClassName());
                result = cls.getAnnotation(targetAnnotation);
            } catch(ClassNotFoundException ex) {
                // this class was not found, maybe annonimous class? just ignore and keep looking
            }
            if (result!=null) {
                return cls;
            }
        }

        GherkinAssert.failedToFoundATestClassWithAnnotation(stack, targetAnnotation);
        return null;
    }

    /*********************************
     * Public methods - Scenario and Step types
     *********************************/

    // init scenario level
    public static void scenario(String description, Runnable code) {
        GherkinProgress progress = getProgress();
        // update progress with scenario
        GherkinScenario scenario = new GherkinScenario(description);
        progress.updateScenario(scenario);

        // handle running the scenario
        try {
            if (getBackground()!=null) {
                getBackground().run();
            }
            code.run();
        } catch (JCCannotContinueException e) {
            progress.updateException(e);
            throw e;
        } catch (Exception | Error e) {
            progress.updateException(e);
            throw GherkinAssert.createClearExceptionFrom(e, progress);
        } finally {
            progress.updateScenario(null);
            progress.report();
        }
    }

    public static void background(Runnable code) {
        Class<?> currentTestClass = findCalledClassWithAnnotation(Feature.class);
        // you can call it several times, only first time counts...
        if (getBackground() == null) {
            backgroundMap.put(currentTestClass, code);
        }
    }

    public static void given(String stepDesc) {
        GherkinStep step = new GherkinStep("Given", stepDesc);
        getProgress().updateStep(step);
    }

    public static void when(String stepDesc) {
        GherkinStep step = new GherkinStep("When", stepDesc);
        getProgress().updateStep(step);
    }

    public static void then(String stepDesc) {
        GherkinStep step = new GherkinStep("Then", stepDesc);
        getProgress().updateStep(step);
    }

    public static void and(String stepDesc) {
        GherkinStep step = new GherkinStep("And", stepDesc);
        getProgress().updateStep(step);
    }

    public static void but(String stepDesc) {
        GherkinStep step = new GherkinStep("But", stepDesc);
        getProgress().updateStep(step);
    }

    public static void finished() {
        getProgress().updateFeature(null);
    }
}
