package testExamples;

import com.hpe.jc.JC;
import com.hpe.jc.JCPlugin;
import com.hpe.jc.plugins.JCPValidateFlowBy;
import com.hpe.jc.plugins.JCTimePlugin;
import com.hpe.jc.plugins.OctaneFormatter.JCOctaneCucumberFormatter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by koreny on 3/20/2017.
 */


public class TestDefects {

    public static JC jc = new JC(
            TestDefects.class,
            new JCPlugin[]{new JCPValidateFlowBy("/gherkin3.feature"), new JCOctaneCucumberFormatter(), new JCTimePlugin()},
            "F");

    @Before
    public void background() {
        jc.background(()-> {
            jc.given("G");
        });
    }

    @Test
    public void myTest() {
        jc.scenario("S", ()->{
            jc.when("W");
        });
    }

    @AfterClass
    public static void closeLog() {

        jc.finished();
    }

}

