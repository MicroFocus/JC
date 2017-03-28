import com.hpe.JC;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by koreny on 3/20/2017.
 */

public class MyTestsTest {

    public static JC jc = new JC(MyTestsTest.class, "c:\\gherkin.feature",
            "hello world");


    @Test
    public void myTest() {
/*        jc.scenario("This is the first scenario", ()->{

            jc.given( "I have a scenario");

            jc.when("I run my junit test");

            jc.then("I can do this");
        });
    }

    @Test
    public void thisIsTheSecondScenario() {
        jc.scenario("this is the second scenario", ()->{
            jc.given ("another try");
            jc.when ("I do something");
            jc.then ("it happens");
        });*/
    }

    /*
    @Test
    public void myTest2() {
        jc.scenario("this is the second <p1> scenario", ()-> {

            jc.given("another try");

            jc.when("I do something");
            Assert.assertEquals(1,2);

            jc.then("it happens");

            //jc.and("bla!");

        });

    }
*/
    @AfterClass
    public static void closeLog() {
        jc.finished();
    }

}

