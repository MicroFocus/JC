package unitTests; /**
 * Created by koreny on 3/20/2017.
 */
import static com.microfocus.jc.JC.*;

import com.microfocus.jc.JCPlugin;
import com.microfocus.jc.plugins.Feature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testUtils.LogPlugin;

@SuppressWarnings("ALL")
@Feature("1")
public class JCCoreTests {

    static LogPlugin log = null;

    @Before
    public void before() {
        log = new LogPlugin();
        reset(new JCPlugin[] {log});
    }

    @Test
    public void TestBasicFlow() {
        scenario("11", ()->{
            given("111");
            when("112");
            then("113");
        });
        scenario("12", ()->{
            given("121");
            when("122");
            then("123");
        });
        finished();

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
        try {
            scenario("11", ()->{
                if (true==true) throw new RuntimeException("error");
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable ex) { }
        finished();

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
        try {
            scenario("11", ()->{
                given("111");
                if (true==true) throw new RuntimeException("error");
                when("112");
                then("113");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable ex) { }
        finished();

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
        try {
            scenario("11", ()->{
                given("111");
                when("112");
                if (true==true) throw new RuntimeException("error");
                then("113");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable ex) { }
        finished();

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
        try {
            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
                if (true==true) throw new RuntimeException("error");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable ex) { }
        finished();

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
        try {
            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                if (true==true) throw new RuntimeException("error");
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable ex) { }
        finished();

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
        try {
            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                given("121");
                if (true==true) throw new RuntimeException("error");
                when("122");
                then("123");
            });

        } catch (Throwable ex) { }
        finished();

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
        try {
            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                given("121");
                when("122");
                if (true==true) throw new RuntimeException("error");
                then("123");
            });

        } catch (Throwable ex) { }
        finished();

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
        try {
            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                given("121");
                when("122");
                then("123");
                if (true==true) throw new RuntimeException("error");
            });

        } catch (Throwable ex) { }
        finished();

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
        try {
            if (true==true) throw new RuntimeException("error");
            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) { }

        try {
            scenario("12", ()->{
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable ex) { }
        finished();

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

}
