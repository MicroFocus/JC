package com.microfocus.jc;

import com.microfocus.jc.errors.GherkinAssert;

/**
 * Created by koreny on 3/25/2017.
 */
public class JCCannotContinueException extends RuntimeException {

    JCPlugin originPlugin;
    public GherkinAssert.ERROR_TYPES errorId;
    public JCCannotContinueException(String message) {
        super(message);
    }

    public JCCannotContinueException(String message, GherkinAssert.ERROR_TYPES id) {
        super(message);
        errorId = id;
    }

    public JCCannotContinueException(String message, Exception ex, GherkinAssert.ERROR_TYPES id) {
        super(message, ex);
        errorId = id;
    }

    public JCCannotContinueException() {
        super();
    }

}
