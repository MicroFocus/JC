package com.hpe.jc.plugins;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by koreny on 6/30/2017.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureFileAt {
    String value() default "";
}
