package com.microfocus.jc.gherkin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koreny on 3/31/2017.
 */
public abstract class GherkinBaseEntity implements IJCDescription, IJCExceptionHolder, IJCPluginDataHolder, IPrintMyself {
    private String description;

    // when the plugin itself has a bug... logged but do not stop the test
    private ArrayList<Throwable> pluginExceptions = new ArrayList<>();

    // when the test result is failure. used as indication that this part of the test has a failure
    private ArrayList<Throwable> testExceptions = new ArrayList<>();

    // when plugin need to stop execution. Used in syntax validation plugin.
    // this is supposed to show a different error, without flow of event and BOOM...
    private ArrayList<Throwable> fatalExceptions = new ArrayList<>();

    GherkinBaseEntity(String description) {
        this.description = description;
    }

    /*************************************
     IJCDescription implementation
     *************************************/

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /*************************************
     IJCExceptionHolder implementation
     *************************************/

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

    public boolean hasMeaningfulExceptions() {
        return (getTestExceptions().size() + getFatalExceptions().size()) > 0;
    }

    /*************************************
     IJCPluginDataHolder implementation
     *************************************/

    HashMap<Class, Object> pluginData = new HashMap<>();
    HashMap<Class, HashMap<String, Object>> pluginDataWithKey = new HashMap<>();

    @Override
    public void setData(Class plugin, String key, Object data)
    {
        if (!pluginDataWithKey.containsKey(plugin)) {
            HashMap<String, Object> newHash = new HashMap<String, Object>();
            pluginDataWithKey.put(plugin, newHash);
        }
        pluginDataWithKey.get(plugin).put(key, data);
    }

    @Override
    public Object getData(Class plugin, String key)
    {
        return pluginDataWithKey.get(plugin).get(key);
    }

}
