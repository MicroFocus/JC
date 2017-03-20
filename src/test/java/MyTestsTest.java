import com.hpe.Feature;
import com.hpe.JC;
import com.hpe.Scenario;
import com.hpe.JCRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by koreny on 3/20/2017.
 */

@Feature("./gherkin.feature")
@RunWith(JCRunner.class)
public class MyTestsTest {

    @Test
    @Scenario("As a user I want to run a junit test in Gherkin style")
    public void myTest() {
        JC.Given("this actually works");

        JC.When("I run this thing");

        JC.Then("I want to see a report");
    }

    @Test
    @Scenario("As a user I want to run another scenario in the same feature file")
    public void myTest2() {
        JC.Given("this actually works twice");

        JC.When("I run this thing again");

        JC.Then("Ill really get crazy");
        Assert.assertEquals(1,2);
    }

}