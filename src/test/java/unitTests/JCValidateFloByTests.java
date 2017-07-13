package unitTests; /**
 * Created by koreny on 3/20/2017.
 */
import static com.hpe.jc.JC.*;
import com.hpe.jc.errors.GherkinAssert;
import com.hpe.jc.errors.JCException;
import com.hpe.jc.JCCannotContinueException;
import com.hpe.jc.JCPlugin;
import com.hpe.jc.plugins.Feature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testUtils.ValidateMock;

@SuppressWarnings("ALL")
@Feature("1")
public class JCValidateFloByTests {

    ValidateMock validate = null;
    final String script =
            "Feature: 1\n" +
            "Scenario: 11\n" +
            "Given 111\n" +
            "When 112\n" +
            "Then 113\n" +
            "Scenario: 12\n" +
            "Given 121\n" +
            "When 122\n" +
            "Then 123\n";

    @Before
    public void before() {
        validate = new ValidateMock(script);
        reset(new JCPlugin[] {validate});
    }

    @Test
    public void TestBasicFlowShouldWork() {
         scenario("11", ()->{
            given("111");
            when("112");
            then("113");
        });
        scenario("12", ()->{
            given("121");
            when("122");
            then("123");
        });
        finished();
    }

    @Test
    public void TestErrorInFeatureDescription() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.FEATURES_MISMATCH;
        try {
            validate = new ValidateMock(
                "Feature: XXXXX\n" +
                "Scenario: 11\n" +
                "Given 111\n" +
                "When 112\n" +
                "Then 113\n" +
                "Scenario: 12\n" +
                "Given 121\n" +
                "When 122\n" +
                "Then 123\n");
            reset(new JCPlugin[] {validate});

            scenario("11", () -> {
                given("111");
                when("112");
                then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(ex.errorId, expectedId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingScenario() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.SCENARIO_TOO_FEW;
        try {
            scenario("11", () -> {
                given("111");
                when("112");
                then("113");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(ex.errorId, expectedId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorTooManyScenarious() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.SCENARIO_MISMATCH;

        try {
            scenario("11", () -> {
                given("111");
                when("112");
                then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            scenario("13", () -> {
                given("131");
                when("132");
                then("133");
            });

            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(ex.errorId, expectedId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorDifferentScenarioDescription() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.SCENARIO_MISMATCH;
        try {
            scenario("error", () -> {
                given("111");
                when("112");
                then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(ex.errorId, expectedId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingStep() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            scenario("11", () -> {
                //given("111");
                when("112");
                then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorDifferentStepType() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            scenario("11", () -> {
                when("111");
                when("112");
                then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorDifferentStepDescription() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            scenario("11", () -> {
                given("error");
                when("112");
                then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingStep2() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            scenario("11", () -> {
                given("111");
                //when("112");
                then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingStep3() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_TOO_FEW;
        try {
            scenario("11", () -> {
                given("111");
                when("112");
                //then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingStepLastStepLastScenario() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_TOO_FEW;
        try {
            scenario("11", () -> {
                given("111");
                when("112");
                then("113");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                //then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorTooManySteps() {
        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_TOO_MANY;
        try {
            scenario("11", () -> {
                given("111");
                when("112");
                then("113");
                then("error");
            });
            scenario("12", () -> {
                given("121");
                when("122");
                then("123");
            });
            finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestRuntimeErrorIn11() {
        try {
            scenario("11", ()->{
                if (true==true) throw new RuntimeException("error");
                given("111");
                when("112");
                then("113");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        scenario("12", ()->{
            given("121");
            when("122");
            then("123");
        });
        finished();
    }

    @Test
    public void TestRuntimeErrorIn111() {
        try {
            scenario("11", ()->{
                given("111");
                if (true==true) throw new RuntimeException("error");
                when("112");
                then("113");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        scenario("12", ()->{
            given("121");
            when("122");
            then("123");
        });
        finished();
    }

    @Test
    public void TestRuntimeErrorIn112() {
        try {
            scenario("11", ()->{
                given("111");
                when("112");
                if (true==true) throw new RuntimeException("error");
                then("113");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        scenario("12", ()->{
            given("121");
            when("122");
            then("123");
        });
        finished();
    }

    @Test
    public void TestRuntimeErrorIn113() {
        try {
            scenario("11", ()->{
                given("111");
                when("112");
                then("113");
                if (true==true) throw new RuntimeException("error");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        scenario("12", ()->{
            given("121");
            when("122");
            then("123");
        });
        finished();
    }

    @Test
    public void TestRuntimeErrorIn12() {
        scenario("11", ()->{
            given("111");
            when("112");
            then("113");
        });
        try {
            scenario("12", ()->{
                if (true==true) throw new RuntimeException("error");
                given("121");
                when("122");
                then("123");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        finished();
    }

    @Test
    public void TestRuntimeErrorIn123() {
        scenario("11", ()->{
            given("111");
            when("112");
            then("113");
        });
        try {
            scenario("12", ()->{
                given("121");
                when("122");
                then("123");
                if (true==true) throw new RuntimeException("error");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        finished();
    }

}
