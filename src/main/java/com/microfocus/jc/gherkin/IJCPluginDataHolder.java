package com.microfocus.jc.gherkin;

/**
 * Created by koreny on 4/1/2017.
 */
public interface IJCPluginDataHolder {
    void setData(Class plugin, String key, Object data);
    Object getData(Class plugin, String key);

}
