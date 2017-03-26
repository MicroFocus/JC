import com.hpe.Feature;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by koreny on 3/20/2017.
 */

public class MyTestsTest {

    public static Feature log = new Feature(MyTestsTest.class, "c:\\gherkin.feature",
            "hello world");

    @Test
    public void myTest() {
        log.scenario("This is the first scenario", ()->{
            log.given( "I have a scenario");

            log.when("I run my junt test");

            //log.then("I can do this");
        });
    }

    @Test
    public void myTest2() {
        log.scenario("this is the second scenario", ()-> {

            log.given("another try");

            log.when("I do something");

            log.then("it happens");

            //log.and("bla!");
            Assert.assertEquals(1,2);

        });
    }

    @AfterClass
    public static void closeLog() {
        log.finished();
    }

}

