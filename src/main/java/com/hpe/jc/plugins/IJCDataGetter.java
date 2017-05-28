package com.hpe.jc.plugins;

import com.hpe.jc.gherkin.GherkinBaseEntity;

/**
 * Created by koreny on 4/2/2017.
 */
public interface IJCDataGetter {
    Object getData(GherkinBaseEntity entity, String propName);
}
