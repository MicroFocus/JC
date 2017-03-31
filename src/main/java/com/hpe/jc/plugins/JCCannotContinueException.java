package com.hpe.jc.plugins;

/**
 * Created by koreny on 3/25/2017.
 */
public class JCCannotContinueException extends RuntimeException {

    JCPlugin originPlugin;
    public JCCannotContinueException(String message) {
        super(message);
    }

    public JCCannotContinueException() {
        super();
    }

}
