package testExamples;

import com.microfocus.jc.plugins.Feature;
import com.microfocus.jc.plugins.FeatureFileAt;
import org.junit.AfterClass;
import org.junit.Test;
import static com.microfocus.jc.JC.*;

/**
 * Created by koreny on 3/20/2017.
 */

@Feature("hello world")
@FeatureFileAt("/gherkin.feature")
public class SimpleScenarios {


    @Test
    public void myTest() {
        scenario("This is the first scenario", ()->{
            given( "I have a scenario");

            when("I run my junit test");

            then("I can do this");
        });
    }

    @Test
    public void myTest2() {
        scenario("this is the second scenario", ()-> {

            given("another try");

            when("I do something");
            //Assert.assertEquals(1,2);

            then("it happens");

        });

    }


    @Test
    public void test() {

    }

    @AfterClass
    public static void closeLog() {
        finished();
    }

}

