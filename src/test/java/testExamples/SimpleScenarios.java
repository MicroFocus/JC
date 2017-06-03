package testExamples;

import com.hpe.jc.JC;
import com.hpe.jc.JCPlugin;
import com.hpe.jc.plugins.JCPValidateFlowBy;
import com.hpe.jc.plugins.JCTimePlugin;
import com.hpe.jc.plugins.OctaneFormatter.JCOctaneCucumberFormatter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by koreny on 3/20/2017.
 */


public class SimpleScenarios {

    public static JC jc = new JC(
            SimpleScenarios.class,
            new JCPlugin[]{new JCPValidateFlowBy("/gherkin.feature"), new JCOctaneCucumberFormatter(), new JCTimePlugin()},
            "hello world");

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
            Assert.assertEquals(1,2);

            jc.then("it happens");

        });

    }

    @AfterClass
    public static void closeLog() {
        jc.finished();
    }

}

