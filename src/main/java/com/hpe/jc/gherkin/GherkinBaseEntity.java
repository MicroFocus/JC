package com.hpe.jc.gherkin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koreny on 3/31/2017.
 */
public abstract class GherkinBaseEntity implements IJCDescription, IJCExceptionHolder, IJCPluginDataHolder {
    private String description;

    // when the plugin has a bug... logged but do not stop the test
    private ArrayList<Throwable> pluginExceptions = new ArrayList<>();

    // when the test result is fail. used as indication that this part of the test has a failure
    private ArrayList<Throwable> testExceptions = new ArrayList<>();

    // when plugin want to stop execution. Used in syntax validation plugin. this is supposed to show a different error, without flow
    private ArrayList<Throwable> fatalExceptions = new ArrayList<>();

    GherkinBaseEntity(String description) {
        this.description = description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void addPluginException(Throwable ex) {
        pluginExceptions.add(ex);
    }

    @Override
    public void addTestException(Throwable ex) {
        testExceptions.add(ex);
    }

    @Override
    public void addFatalException(Throwable ex) {
        fatalExceptions.add(ex);
    }

    @Override
    public ArrayList<Throwable> getPluginExceptions() {
        return pluginExceptions;
    }

    @Override
    public ArrayList<Throwable> getTestExceptions() {
        return testExceptions;
    }

    @Override
    public ArrayList<Throwable> getFatalExceptions() {
        return fatalExceptions;
    }


    HashMap<Class, Object> pluginData = new HashMap<>();

    @Override
    public void setData(Class plugin, Object data) {
        pluginData.put(plugin, data);
    }

    @Override
    public Object getData(Class plugin) {
        return pluginData.get(plugin);
    }

}
