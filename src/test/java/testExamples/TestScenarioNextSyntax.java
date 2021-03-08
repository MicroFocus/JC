package testExamples;

import static com.microfocus.jc.JC.*;
import com.microfocus.jc.plugins.Feature;
import com.microfocus.jc.plugins.FeatureFileAt;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

@FeatureFileAt("/gherkin2.feature")
@Feature("hello world")
public class TestScenarioNextSyntax {

    public static int counter;
    @Before
    public void before() {
        background(()-> {
            counter++;
            given("I am logged in");
            and("I have 3 tests");
            //if (counter==2) Assert.assertEquals(1,2);
            but("do not have permissions to delete");
        });
    }

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
            then("it happens");
        });

    }

    @AfterClass
    public static void closeLog() {
        finished();
    }

}

