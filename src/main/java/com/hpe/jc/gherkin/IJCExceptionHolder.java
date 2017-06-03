package com.hpe.jc.gherkin;

import java.util.ArrayList;

/**
 * Created by koreny on 3/31/2017.
 */
public interface IJCExceptionHolder {
    void addPluginException(Throwable ex);
    void addTestException(Throwable ex);
    void addFatalException(Throwable ex);

    ArrayList<Throwable> getPluginExceptions();
    ArrayList<Throwable> getTestExceptions();
    ArrayList<Throwable> getFatalExceptions();

}
