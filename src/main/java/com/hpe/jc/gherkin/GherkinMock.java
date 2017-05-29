package com.hpe.jc.gherkin;

import java.util.ArrayList;

/**
 * Created by koreny on 5/29/2017.
 */
public class GherkinMock implements IJCExceptionHolder {

    ArrayList<Throwable> pluginExArray = new ArrayList<>();
    ArrayList<Throwable> testExArray = new ArrayList<>();
    ArrayList<Throwable> fatalExArray = new ArrayList<>();

    @Override
    public void addPluginException(Throwable ex) {
        pluginExArray.add(ex);
    }

    @Override
    public void addTestException(Throwable ex) {
        testExArray.add(ex);
    }

    @Override
    public void addFatalException(Throwable ex) {
        fatalExArray.add(ex);
    }

    @Override
    public ArrayList<Throwable> getPluginExceptions() {
        return pluginExArray;
    }

    @Override
    public ArrayList<Throwable> getTestExceptions() {
        return testExArray;
    }

    @Override
    public ArrayList<Throwable> getFatalExceptions() {
        return fatalExArray;
    }
}
