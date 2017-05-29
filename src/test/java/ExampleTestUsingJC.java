import com.hpe.jc.JC;
import com.hpe.jc.plugins.OctaneFormatter.JCOctaneCucumberFormatter;
import com.hpe.jc.plugins.JCPValidateFlowBy;
import com.hpe.jc.JCPlugin;
import com.hpe.jc.plugins.JCTimePlugin;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by koreny on 3/20/2017.
 */


public class ExampleTestUsingJC {

    public static JC jc = new JC(
            ExampleTestUsingJC.class,
            new JCPlugin[]{new JCPValidateFlowBy("/gherkin2.feature"), new JCOctaneCucumberFormatter(), new JCTimePlugin()},
            "hello world");


    @Test
    public void myTest() {
        jc.scenario("This is the first scenario", ()->{

            jc.given( "I have a scenario");
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jc.when("I run my junit test");
            try {
                Thread.sleep(32);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jc.then("I can do this");
            try {
                Thread.sleep(64);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

