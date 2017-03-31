/**
 * Created by koreny on 3/20/2017.
 */
import com.hpe.jc.gherkin.GherkinProgress;
import com.hpe.jc.JC;
import com.hpe.jc.plugins.JCPlugin;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class JCTests {

    @Test
    public void TestBasicFlow() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        jc.scenario("11", ()->{
            jc.given("111");
            jc.when("112");
            jc.then("113");
        });
        jc.scenario("12", ()->{
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();

        String[] expectedFlow = new String[] {
            "s1",
                "s11",
                    "s111",
                    "e111",
                    "s112",
                    "e112",
                    "s113",
                    "e113",
                "e11",
                "s12",
                    "s121",
                    "e121",
                    "s122",
                    "e122",
                    "s123",
                    "e123",
                "e12",
            "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorAfter11() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.scenario("11", ()->{
                if (true==true) throw new RuntimeException("error");
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "f11",
                "e11",
                "s12",
                "s121",
                "e121",
                "s122",
                "e122",
                "s123",
                "e123",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorAfter111() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                if (true==true) throw new RuntimeException("error");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s111",
                "f111",
                "e111",
                "e11",
                "s12",
                "s121",
                "e121",
                "s122",
                "e122",
                "s123",
                "e123",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorAfter112() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                if (true==true) throw new RuntimeException("error");
                jc.then("113");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s111",
                "e111",
                "s112",
                "f112",
                "e112",
                "e11",
                "s12",
                "s121",
                "e121",
                "s122",
                "e122",
                "s123",
                "e123",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorAfter113() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
                if (true==true) throw new RuntimeException("error");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "f113",
                "e113",
                "e11",
                "s12",
                "s121",
                "e121",
                "s122",
                "e122",
                "s123",
                "e123",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorAfter12() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                if (true==true) throw new RuntimeException("error");
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "e113",
                "e11",
                "s12",
                "f12",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorAfter121() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                jc.given("121");
                if (true==true) throw new RuntimeException("error");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "e113",
                "e11",
                "s12",
                "s121",
                "f121",
                "e121",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorAfter122() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                jc.given("121");
                jc.when("122");
                if (true==true) throw new RuntimeException("error");
                jc.then("123");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "e113",
                "e11",
                "s12",
                "s121",
                "e121",
                "s122",
                "f122",
                "e122",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorAfter123() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                jc.given("121");
                jc.when("122");
                jc.then("123");
                if (true==true) throw new RuntimeException("error");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "e113",
                "e11",
                "s12",
                "s121",
                "e121",
                "s122",
                "e122",
                "s123",
                "f123",
                "e123",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestFlowWithErrorBefore11() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            if (true==true) throw new RuntimeException("error");
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) { }

        try {
            jc.scenario("12", ()->{
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable ex) { }
        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                // This is an edge case. no syntax validator plugin - so nobody will complain...
                "s12",
                "s121",
                "e121",
                "s122",
                "e122",
                "s123",
                "e123",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    class LogPlugin extends JCPlugin {

        public ArrayList<String> log = new ArrayList<>();

        @Override
        public void onFeatureStart(GherkinProgress progress) {
            log.add("s"+progress.getCurrentFeature().getDescription());
        }

        @Override
        public void onFeatureEnd(GherkinProgress progress) {
            log.add("e"+progress.getCurrentFeature().getDescription());

        }

        @Override
        public void onScenarioStart(GherkinProgress progress) {
            log.add("s"+progress.getCurrentScenario().getDescription());

        }

        @Override
        public void onScenarioEnd(GherkinProgress progress) {
            log.add("e"+progress.getCurrentScenario().getDescription());

        }

        @Override
        public void onStepStart(GherkinProgress progress) {
            log.add("s"+progress.getCurrentStep().getDescription());

        }

        @Override
        public void onStepEnd(GherkinProgress progress) {
            log.add("e"+progress.getCurrentStep().getDescription());

        }

        @Override
        public void onStepFailure(GherkinProgress progress, Throwable ex) {
            if (progress.getCurrentStep()!=null) {
                log.add("f"+progress.getCurrentStep().getDescription());
            } else {
                log.add("f"+progress.getCurrentScenario().getDescription());
            }

        }
    }

}
