package unitTests; /**
 * Created by koreny on 3/20/2017.
 */
import com.microfocus.jc.JCPlugin;
import com.microfocus.jc.plugins.Feature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testUtils.LogPlugin;

import java.util.ArrayList;

import static com.microfocus.jc.JC.*;

@SuppressWarnings("ALL")
@Feature("1")
public class JCBeforeSyntaxTests {

    static LogPlugin log = null;

    @Before
    public void before() {
        log = new LogPlugin();
        reset(new JCPlugin[] {log});
    }

    @Test
    public void TestBasicFlow() {
        background(()->{
            given("001");
            when("002");
            then("003");

        });
        scenario("11", ()->{
            given("111");
            when("112");
            then("113");
        });
        background(()->{
            given("001");
            when("002");
            then("003");

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
                    "s001",
                    "e001",
                    "s002",
                    "e002",
                    "s003",
                    "e003",
                    "s111",
                    "e111",
                    "s112",
                    "e112",
                    "s113",
                    "e113",
                "e11",
                "s12",
                    "s001",
                    "e001",
                    "s002",
                    "e002",
                    "s003",
                    "e003",
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
        final ArrayList<Object> bool = new ArrayList<>();
        try {
            background(()-> {
                if (bool.size()==0) {
                    bool.add(new Object());
                    throw new RuntimeException("Error");
                }
                given("001");
                when("002");
                then("003");

            });

            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) {}

        try {
            background(()->{
                given("001");
                when("002");
                then("003");

            });

            scenario("12", ()-> {
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable e) {}

        finished();

        String[] expectedFlow = new String[] {
                "s1",
                    "s11",
                    "f11",
                    "e11",
                    "s12",
                        "s001",
                        "e001",
                        "s002",
                        "e002",
                        "s003",
                        "e003",
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
        try {
            final ArrayList<Object> bool = new ArrayList<>();
            background(()-> {
                given("001");
                if (bool.size()==0) {
                    bool.add(new Object());
                    throw new RuntimeException("error");
                }
                when("002");
                then("003");

            });

            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) {}

        try {

            scenario("12", ()-> {
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable e) {}

        finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s001",
                "f001",
                "e001",
                "e11",
                "s12",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
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
        try {
            final ArrayList<Object> bool = new ArrayList<>();
            background(()-> {
                given("001");
                when("002");
                if (bool.size()==0) {
                    bool.add(new Object());
                    throw new RuntimeException("error");
                }
                then("003");

            });

            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) {}

        try {

            scenario("12", ()-> {
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable e) {}

        finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s001",
                "e001",
                "s002",
                "f002",
                "e002",
                "e11",
                "s12",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
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
        try {
            final ArrayList<Object> bool = new ArrayList<>();
            background(()-> {
                given("001");
                when("002");
                then("003");
                if (bool.size()==0) {
                    bool.add(new Object());
                    throw new RuntimeException("error");
                }

            });

            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) {}

        try {

            scenario("12", ()-> {
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable e) {}

        finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "f003",
                "e003",
                "e11",
                "s12",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
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
        try {
            background(()-> {
                given("001");
                when("002");
                then("003");
            });

            scenario("11", ()->{
                given("111");
                if (true == true) throw new RuntimeException("error");
                when("112");
                then("113");
            });
        } catch (Throwable e) {}

        try {
            background(()->{
                given("001");
                when("002");
                then("003");

            });

            scenario("12", ()-> {
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable e) {}

        finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "s111",
                "f111",
                "e111",
                "e11",
                "s12",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
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
        final ArrayList<Object> bool = new ArrayList<>();
        try {
            background(()-> {
                given("001");
                if (bool.size()>0) {
                    throw new RuntimeException("Error");
                } else {
                    when("002");
                }
                then("003");
                bool.add(new Object());
            });

            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) {}

        try {
            background(()->{
                given("001");
                when("002");
                then("003");

            });

            scenario("12", ()-> {
                given("121");
                when("122");
                then("123");
            });

        } catch (Throwable e) {}

        finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "e113",
                "e11",
                "s12",
                "s001",
                "f001",
                "e001",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

    @Test
    public void TestExceptionInSecondScenario() {
        try {
            background(()-> {
                given("001");
                when("002");
                then("003");
            });

            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
            });
        } catch (Throwable e) {}

        try {
            background(()->{
                given("001");
                when("002");
                then("003");

            });

            scenario("12", ()-> {
                given("121");
                if (true == true) throw new RuntimeException("error");
                when("122");
                then("123");
            });

        } catch (Throwable e) {}

        finished();

        String[] expectedFlow = new String[] {
                "s1",
                "s11",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "s111",
                "e111",
                "s112",
                "e112",
                "s113",
                "e113",
                "e11",
                "s12",
                "s001",
                "e001",
                "s002",
                "e002",
                "s003",
                "e003",
                "s121",
                "f121",
                "e121",
                "e12",
                "e1",
        };

        Assert.assertArrayEquals(expectedFlow, log.log.toArray());
    }

}
