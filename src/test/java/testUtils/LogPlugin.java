package testUtils;

import com.hpe.jc.JCPlugin;
import org.junit.Assert;

import java.util.ArrayList;

/**
 * Created by koreny on 6/2/2017.
 */
public class LogPlugin extends JCPlugin {

    public ArrayList<String> log = new ArrayList<>();


    @Override
    protected void onInit() { }


    @Override
    protected void onBackgroundStart() {
        log.add("s"+progress.getCurrentScenario().getDescription());

    }

    @Override
    protected void onBackgroundEnd() {
        log.add("e"+progress.getCurrentScenario().getDescription());

    }

    @Override
    protected void onEndOfAny() {

    }

    @Override
    protected void onStartOfAny() {

    }

    protected void onFeatureStart() {
        Assert.assertEquals(progress.getCurrentFeature(), progress.getCurrent());
        log.add("s"+progress.getCurrentFeature().getDescription());
    }

    protected void onFeatureEnd() {
        Assert.assertEquals(progress.getCurrentFeature(), progress.getCurrent());
        log.add("e"+progress.getCurrentFeature().getDescription());

    }

    protected void onScenarioStart() {
        Assert.assertEquals(progress.getCurrentScenario(), progress.getCurrent());
        log.add("s"+progress.getCurrentScenario().getDescription());

    }

    protected void onScenarioEnd() {
        Assert.assertEquals(progress.getCurrentScenario(), progress.getCurrent());
        log.add("e"+progress.getCurrentScenario().getDescription());

    }

    protected void onStepStart() {
        Assert.assertEquals(progress.getCurrentStep(), progress.getCurrent());
        log.add("s"+progress.getCurrentStep().getDescription());

    }

    protected void onStepEnd() {
        Assert.assertEquals(progress.getCurrentStep(), progress.getCurrent());
        log.add("e"+progress.getCurrentStep().getDescription());

    }

    protected void onStepFailure(Throwable ex) {
        if (progress.getCurrentStep()!=null) {
            log.add("f"+progress.getCurrentStep().getDescription());
        } else {
            log.add("f"+progress.getCurrentScenario().getDescription());
        }

    }
}