package com.hpe;

/**
 * Created by koreny on 3/29/2017.
 */

public abstract class JCPlugin {
    public abstract void onFeatureStart(GherkinProgress progress);
    public abstract void onFeatureEnd(GherkinProgress progress);
    public abstract void onScenarioStart(GherkinProgress progress);
    public abstract void onScenarioEnd(GherkinProgress progress);
    public abstract void onStepStart(GherkinProgress progress);
    public abstract void onStepEnd(GherkinProgress progress);
    public abstract void onStepFailure(GherkinProgress progress, Throwable ex);
}
