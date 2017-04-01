package com.hpe.jc;

/**
 * Created by koreny on 3/29/2017.
 */

public abstract class JCPlugin {

    protected GherkinProgress progress;

    protected void setPluginData(Object data) {
        progress.getCurrent().setData(this.getClass(), data);
    }

    protected Object getPluginData() {
        return progress.getCurrent().getData(this.getClass());
    }

    void setProgress(GherkinProgress progress) {
        this.progress = progress;
    }

    protected abstract void onFeatureStart();
    protected abstract void onFeatureEnd();
    protected abstract void onScenarioStart();
    protected abstract void onScenarioEnd();
    protected abstract void onStepStart();
    protected abstract void onStepEnd();
    protected abstract void onStepFailure(Throwable ex);

}
