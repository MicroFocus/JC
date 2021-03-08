package com.hpe.jc.plugins;

import com.hpe.jc.JCPlugin;
import com.hpe.jc.gherkin.GherkinBaseEntity;

import java.util.Date;

/**
 * Created by koreny on 4/1/2017.
 */
public class JCPStepTimer extends JCPlugin {

    /*********************************
     * Plugin Members
     *********************************/

    public static final String END_TIME = "END_TIME";
    public static final String START_TIME = "START_TIME";

    /*********************************
     * Plugin getters
     *********************************/

    public static Date getStartTime(GherkinBaseEntity gherkinItem) {
        return (Date)gherkinItem.getData(JCPStepTimer.class, START_TIME);
    }

    public static Date getEndTime(GherkinBaseEntity gherkinItem) {
        return (Date)gherkinItem.getData(JCPStepTimer.class, END_TIME);
    }

    /*********************************
     * Plugin implementation
     *********************************/

    @Override
    protected void onInit() { }
///*
//    @Override
//    protected void onBackgroundStart() {
//
//    }
//
//    @Override
//    protected void onBackgroundEnd() {
//
//    }*/

    @Override
    protected void onEndOfAny() {
        progress.getCurrent().setData(this.getClass(), END_TIME, new Date());
    }

    @Override
    protected void onStartOfAny() {

        progress.getCurrent().setData(this.getClass(), START_TIME, new Date());
    }

    @Override
    protected void onFeatureStart() {
    }

    @Override
    protected void onFeatureEnd() {

    }

    @Override
    protected void onScenarioStart() {

    }

    @Override
    protected void onScenarioEnd() {

    }

    @Override
    protected void onStepStart() {

    }

    @Override
    protected void onStepEnd() {

    }

    @Override
    protected void onStepFailure(Throwable ex) {

    }
}
