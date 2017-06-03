package unitTests; /**
 * Created by koreny on 3/20/2017.
 */
import com.hpe.jc.JC;
import com.hpe.jc.JCPlugin;
import org.junit.Assert;
import org.junit.Test;
import testUtils.LogPlugin;

import java.util.ArrayList;

public class JCBeforeSyntaxTests {

    @Test
    public void TestBasicFlow() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        jc.background("00", ()->{
            jc.given("001");
            jc.when("002");
            jc.then("003");

        });
        jc.scenario("11", ()->{
            jc.given("111");
            jc.when("112");
            jc.then("113");
        });
        jc.background("00", ()->{
            jc.given("001");
            jc.when("002");
            jc.then("003");

        });
        jc.scenario("12", ()->{
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();

        String[] expectedFlow = new String[] {
            "s1",
                "s00",
                    "s001",
                    "e001",
                    "s002",
                    "e002",
                    "s003",
                    "e003",
                "e00",
                "s11",
                    "s111",
                    "e111",
                    "s112",
                    "e112",
                    "s113",
                    "e113",
                "e11",
                "s00",
                    "s001",
                    "e001",
                    "s002",
                    "e002",
                    "s003",
                    "e003",
                "e00",
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
    public void TestExceptionAfter00() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.background("00", ()-> {
                if (true == true) throw new RuntimeException("error");
                jc.given("001");
                jc.when("002");
                jc.then("003");
            });

            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) {}

        try {
            jc.background("00", ()->{
                jc.given("001");
                jc.when("002");
                jc.then("003");

            });

            jc.scenario("12", ()-> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable e) {}

        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                    "s00",
                    "f00",
                    "e00",
                    "s00",
                        "s001",
                        "e001",
                        "s002",
                        "e002",
                        "s003",
                        "e003",
                    "e00",
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
    public void TestExceptionAfter001() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.background("00", ()-> {
                jc.given("001");
                if (true == true) throw new RuntimeException("error");
                jc.when("002");
                jc.then("003");
            });

            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) {}

        try {
            jc.background("00", ()->{
                jc.given("001");
                jc.when("002");
                jc.then("003");

            });

            jc.scenario("12", ()-> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable e) {}

        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s00",
                "s001",
                "f001",
                "e001",
                "e00",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "e00",
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
    public void TestExceptionAfter002() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.background("00", ()-> {
                jc.given("001");
                jc.when("002");
                if (true == true) throw new RuntimeException("error");
                jc.then("003");
            });

            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) {}

        try {
            jc.background("00", ()->{
                jc.given("001");
                jc.when("002");
                jc.then("003");

            });

            jc.scenario("12", ()-> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable e) {}

        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s00",
                "s001",
                "e001",
                "s002",
                "f002",
                "e002",
                "e00",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "e00",
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
    public void TestExceptionAfter003() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.background("00", ()-> {
                jc.given("001");
                jc.when("002");
                jc.then("003");
                if (true == true) throw new RuntimeException("error");
            });

            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) {}

        try {
            jc.background("00", ()->{
                jc.given("001");
                jc.when("002");
                jc.then("003");

            });

            jc.scenario("12", ()-> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable e) {}

        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "f003",
                "e003",
                "e00",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "e00",
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
    public void TestExceptionInScenario() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.background("00", ()-> {
                jc.given("001");
                jc.when("002");
                jc.then("003");
            });

            jc.scenario("11", ()->{
                jc.given("111");
                if (true == true) throw new RuntimeException("error");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) {}

        try {
            jc.background("00", ()->{
                jc.given("001");
                jc.when("002");
                jc.then("003");

            });

            jc.scenario("12", ()-> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable e) {}

        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "e00",
                "s11",
                "s111",
                "f111",
                "e111",
                "e11",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "e00",
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
    public void TestExceptionInBackgroundSecondTime() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.background("00", ()-> {
                jc.given("001");
                jc.when("002");
                jc.then("003");
            });

            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) {}

        try {
            jc.background("00", ()->{
                jc.given("001");
                if (true == true) throw new RuntimeException("error");
                jc.when("002");
                jc.then("003");

            });

            jc.scenario("12", ()-> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable e) {}

        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "e00",
                "s11",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "e113",
                "e11",
                "s00",
                "s001",
                "f001",
                "e001",
                "e00",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestExceptionInSecondScenario() {
        LogPlugin log = new LogPlugin();
        JC jc = new JC(this, new JCPlugin[] {log},"1");
        try {
            jc.background("00", ()-> {
                jc.given("001");
                jc.when("002");
                jc.then("003");
            });

            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (Throwable e) {}

        try {
            jc.background("00", ()->{
                jc.given("001");
                jc.when("002");
                jc.then("003");

            });

            jc.scenario("12", ()-> {
                jc.given("121");
                if (true == true) throw new RuntimeException("error");
                jc.when("122");
                jc.then("123");
            });

        } catch (Throwable e) {}

        jc.finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "e00",
                "s11",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "e113",
                "e11",
                "s00",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "e00",
                "s12",
                "s121",
                "f121",
                "e121",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

}
