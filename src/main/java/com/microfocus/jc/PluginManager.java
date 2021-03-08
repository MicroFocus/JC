package com.microfocus.jc;

import com.microfocus.jc.gherkin.GherkinMock;
import com.microfocus.jc.gherkin.IJCExceptionHolder;
import com.microfocus.jc.plugins.IJCRunPlugin;

import java.util.ArrayList;

public class PluginManager extends JCPlugin {

    ArrayList<JCPlugin> plugins = new ArrayList<>();
    GherkinMock initExceptionHolder = new GherkinMock();

    public void registerPlugins(GherkinProgress progress, JCPlugin[] pluginsArr) {
        this.progress = progress;

        for (JCPlugin plugin : pluginsArr) {
            plugin.setProgress(progress);
            plugins.add(plugin);
            runPlugin(initExceptionHolder, (p)-> p.onInit(), plugin);
        }
    }

    public ArrayList<JCPlugin> getPlugins() {
        return (ArrayList<JCPlugin>) plugins.clone();
    }

    private void run(IJCExceptionHolder entity, IJCRunPlugin code) {

        for (JCPlugin plugin : plugins) {
            runPlugin(entity, code, plugin);
        }
    }

    private void runPlugin(IJCExceptionHolder entity, IJCRunPlugin code, JCPlugin plugin) {

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


    @Override
    protected void onInit() {
        //Init is called on each registration of plugin to allow splitting the registration
    }

    @Override
    protected void onEndOfAny() {

    }

    @Override
    protected void onStartOfAny() {

    }

    protected void onFeatureStart() {
        run(progress.getCurrent(), (plugin)-> plugin.onStartOfAny());
        run(progress.getCurrentFeature(), (plugin)-> plugin.onFeatureStart());
    }

    protected void onFeatureEnd() {
        run(progress.getCurrent(), (plugin)-> plugin.onEndOfAny());
        run(progress.getCurrentFeature(), (plugin)-> plugin.onFeatureEnd());
    }

    protected void onScenarioStart() {
        run(progress.getCurrent(), (plugin)-> plugin.onStartOfAny());
        run(progress.getCurrentScenario(), (plugin)-> plugin.onScenarioStart());
    }

    protected void onScenarioEnd() {
        run(progress.getCurrent(), (plugin)-> plugin.onEndOfAny());
        run(progress.getCurrentScenario(), (plugin)-> plugin.onScenarioEnd());
    }

    protected void onStepStart() {
        run(progress.getCurrent(), (plugin)-> plugin.onStartOfAny());
        run(progress.getCurrentStep(), (plugin)-> plugin.onStepStart());
    }

    protected void onStepEnd() {
        run(progress.getCurrent(), (plugin)-> plugin.onEndOfAny());
        run(progress.getCurrentStep(), (plugin) -> plugin.onStepEnd());
    }

    protected void onStepFailure(Throwable ex) {
        run(progress.getCurrentStep(), (plugin)-> plugin.onStepFailure(ex));
    }

}
