package com.hpe.jc.plugins;

import com.hpe.jc.JCPlugin;

/**
 * Created by koreny on 3/31/2017.
 */

@FunctionalInterface
public interface IJCRunPlugin {
    void run(JCPlugin plugin);
}
