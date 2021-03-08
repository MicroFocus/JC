package com.microfocus.jc.plugins;

import com.microfocus.jc.JCPlugin;

/**
 * Created by koreny on 3/31/2017.
 */

@FunctionalInterface
public interface IJCRunPlugin {
    void run(JCPlugin plugin);
}
