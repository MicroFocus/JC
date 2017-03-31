package com.hpe.jc.errors;

/**
 * Created by koreny on 3/26/2017.
 */
public class JCException extends Error {

    public JCException() {
        super();
    }

    public JCException(String message) {
        super(message);
    }

    public JCException(String message, Throwable orig) {
        super(message, orig);

        this.setStackTrace(orig.getStackTrace());

    }
}
