package com.hpe.jc;

import com.hpe.jc.gherkin.IJCExceptionHolder;
import com.hpe.jc.plugins.IJCRunPlugin;

import java.util.ArrayList;

public class PluginManager extends JCPlugin {

    ArrayList<JCPlugin> plugins = new ArrayList<>();

    public void registerPlugin(JCPlugin plugin) {
        plugins.add(plugin);
    }

    public ArrayList<JCPlugin> getPlugins() {
        return (ArrayList<JCPlugin>) plugins.clone();
    }

    private void run(IJCExceptionHolder entity, IJCRunPlugin code) {

        for (JCPlugin plugin : plugins) {
            try {

                code.run(plugin);
            } catch (JCCannotContinueException ex) {
                ex.originPlugin = plugin;
                entity.addFatalException(ex);
                throw ex;
            } catch (Throwable ex) {
                entity.addPluginException(ex);
            }
        }
    }

    protected void onFeatureStart() {
        run(progress.getCurrentFeature(), (plugin)-> plugin.onFeatureStart());
    }

    protected void onFeatureEnd() {
        run(progress.getCurrentFeature(), (plugin)-> plugin.onFeatureEnd());
    }

    protected void onScenarioStart() {
        run(progress.getCurrentScenario(), (plugin)-> plugin.onScenarioStart());
    }

    protected void onScenarioEnd() {
        run(progress.getCurrentScenario(), (plugin)-> plugin.onScenarioEnd());
    }

    protected void onStepStart() {
        run(progress.getCurrentStep(), (plugin)-> plugin.onStepStart());
    }

    protected void onStepEnd() {
        run(progress.getCurrentStep(), (plugin) -> plugin.onStepEnd());
    }

    protected void onStepFailure(Throwable ex) {
        run(progress.getCurrentStep(), (plugin)-> plugin.onStepFailure(ex));
    }

}
