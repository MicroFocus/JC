package testExamples;

import com.hpe.jc.JC;
import com.hpe.jc.plugins.OctaneFormatter.JCOctaneCucumberFormatter;
import com.hpe.jc.plugins.JCPValidateFlowBy;
import com.hpe.jc.JCPlugin;
import com.hpe.jc.plugins.JCTimePlugin;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by koreny on 3/20/2017.
 */


public class TestScenarioWithBackground {

    public static JC jc = new JC(
            TestScenarioWithBackground.class,
            new JCPlugin[]{new JCPValidateFlowBy("/gherkin2.feature"), new JCOctaneCucumberFormatter(), new JCTimePlugin()},
            "hello world");

    public static int counter;
    @Before
    public void background() {
        jc.background("hello background", ()-> {
            counter++;
            jc.given("I am logged in");
            jc.and("I have 3 tests");
            if (counter==1) Assert.assertEquals(1,2);
            jc.but("do not have permissions to delete");
        });
    }

    @Test
    public void myTest() {
        jc.scenario("This is the first scenario", ()->{
            jc.given( "I have a scenario");
            jc.when("I run my junit test");
            jc.then("I can do this");
        });
    }

    @Test
    public void myTest2() {
        jc.scenario("this is the second scenario", ()-> {
            jc.given("another try");
            jc.when("I do something");
            jc.then("it happens");
        });

    }

    @AfterClass
    public static void closeLog() {
        jc.finished();
    }

}

