package com.hpe.jc.plugins;

import com.hpe.jc.gherkin.GherkinProgress;
import com.hpe.jc.gherkin.IJCExceptionHolder;

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

    public void onFeatureStart(GherkinProgress progress) {
        run(progress.getCurrentFeature(), (plugin)-> plugin.onFeatureStart(progress));
    }

    public void onFeatureEnd(GherkinProgress progress) {
        run(progress.getCurrentFeature(), (plugin)-> plugin.onFeatureEnd(progress));
    }

    public void onScenarioStart(GherkinProgress progress) {
        run(progress.getCurrentScenario(), (plugin)-> plugin.onScenarioStart(progress));
    }

    public void onScenarioEnd(GherkinProgress progress) {
        run(progress.getCurrentScenario(), (plugin)-> plugin.onScenarioEnd(progress));
    }

    public void onStepStart(GherkinProgress progress) {
        run(progress.getCurrentStep(), (plugin)-> plugin.onStepStart(progress));
    }

    public void onStepEnd(GherkinProgress progress) {
        run(progress.getCurrentStep(), (plugin)-> plugin.onStepEnd(progress));
    }

    public void onStepFailure(GherkinProgress progress, Throwable ex) {
        run(progress.getCurrentStep(), (plugin)-> plugin.onStepFailure(progress, ex));
    }

}
