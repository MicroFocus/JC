package com.hpe.jc;

import com.hpe.jc.gherkin.GherkinBaseEntity;
import com.hpe.jc.plugins.IJCDataGetter;

import java.util.HashMap;

/**
 * Created by koreny on 3/29/2017.
 */

public abstract class JCPlugin {

    protected GherkinProgress progress;

    void setProgress(GherkinProgress progress) {
        this.progress = progress;
    }

    protected abstract void onEndOfAny();
    protected abstract void onStartOfAny();
    protected abstract void onFeatureStart();
    protected abstract void onFeatureEnd();
    protected abstract void onScenarioStart();
    protected abstract void onScenarioEnd();
    protected abstract void onStepStart();
    protected abstract void onStepEnd();
    protected abstract void onStepFailure(Throwable ex);
}
