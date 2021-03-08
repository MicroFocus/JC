package testExamples;

import static com.microfocus.jc.JC.*;

import com.microfocus.jc.plugins.Feature;
import com.microfocus.jc.plugins.FeatureFileAt;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by koreny on 3/20/2017.
 */

@Feature("F")
@FeatureFileAt("/gherkin3.feature")
public class TestDefects {

    @Before
    public void before() {
        background(()-> {
            given("G");
        });
    }

    @Test
    public void myTest() {
        scenario("S", ()->{
            when("W");
        });
    }

    @AfterClass
    public static void closeLog() {

        finished();
    }

}

