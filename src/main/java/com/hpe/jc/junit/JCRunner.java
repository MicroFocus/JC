package com.hpe.jc.junit;

import junit.framework.Test;
import junit.framework.TestResult;

import java.io.Console;

/**
 * Created by koreny on 6/28/2017.
 */
public class JCRunner extends junit.extensions.TestDecorator {
    public JCRunner(Test test) {
        super(test);
    }

    //public void run(TestResult result) {

    //}
}
