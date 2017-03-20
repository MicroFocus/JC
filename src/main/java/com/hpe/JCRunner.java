/**
 * Created by koreny on 3/20/2017.
 */
package com.hpe;

        import org.junit.runner.notification.RunNotifier;
        import org.junit.runners.BlockJUnit4ClassRunner;
        import org.junit.runners.model.InitializationError;

public class JCRunner extends BlockJUnit4ClassRunner {

    public JCRunner(Class<?> klass) throws InitializationError {

        super(klass);
    }

    @Override public void run(RunNotifier notifier){
        notifier.addListener(new JCListener());
        super.run(notifier);
    }
}