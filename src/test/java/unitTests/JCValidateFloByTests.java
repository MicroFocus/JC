package unitTests; /**
 * Created by koreny on 3/20/2017.
 */

import com.hpe.jc.JC;
import com.hpe.jc.errors.GherkinAssert;
import com.hpe.jc.errors.JCException;
import com.hpe.jc.JCCannotContinueException;
import com.hpe.jc.JCPlugin;
import org.junit.Assert;
import org.junit.Test;
import testUtils.ValidateMock;

public class JCValidateFloByTests {

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


    @Test
    public void TestBasicFlowShouldWork() {
        ValidateMock validate = new ValidateMock(
                 "Feature: 1\n" +
                        "Scenario: 11\n" +
                        "Given 111\n" +
                        "When 112\n" +
                        "Then 113\n" +
                        "Scenario: 12\n" +
                        "Given 121\n" +
                        "When 122\n" +
                        "Then 123\n"
        );
        JC jc = new JC(this, new JCPlugin[] {validate},"1");
        jc.scenario("11", ()->{
            jc.given("111");
            jc.when("112");
            jc.then("113");
        });
        jc.scenario("12", ()->{
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();
    }

    @Test
    public void TestErrorInFeatureDescription() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.FEATURES_MISMATCH;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "error");
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(ex.errorId, expectedId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingScenario() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.SCENARIO_TOO_FEW;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(ex.errorId, expectedId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorTooManyScenarious() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.SCENARIO_MISMATCH;

        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.scenario("13", () -> {
                jc.given("131");
                jc.when("132");
                jc.then("133");
            });

            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(ex.errorId, expectedId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorDifferentScenarioDescription() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.SCENARIO_MISMATCH;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("error", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(ex.errorId, expectedId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingStep() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                //jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorDifferentStepType() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                jc.when("111");
                jc.when("112");
                jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorDifferentStepDescription() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                jc.given("error");
                jc.when("112");
                jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingStep2() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                jc.given("111");
                //jc.when("112");
                jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingStep3() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_TOO_FEW;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                //jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorMissingStepLastStepLastScenario() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_TOO_FEW;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                //jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestErrorTooManySteps() {
        ValidateMock validate = new ValidateMock(script);

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_TOO_MANY;
        try {
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
                jc.then("error");
            });
            jc.scenario("12", () -> {
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
            jc.finished();
        } catch (JCCannotContinueException ex) {
            Assert.assertEquals(expectedId, ex.errorId);
            return;
        }

        throw new RuntimeException("Got no error, expected error with ID " + expectedId.toString());
    }

    @Test
    public void TestRuntimeErrorIn11() {
        ValidateMock validate = new ValidateMock(
                "Feature: 1\n" +
                        "Scenario: 11\n" +
                        "Given 111\n" +
                        "When 112\n" +
                        "Then 113\n" +
                        "Scenario: 12\n" +
                        "Given 121\n" +
                        "When 122\n" +
                        "Then 123\n"
        );
        JC jc = new JC(this, new JCPlugin[] {validate},"1");
        try {
            jc.scenario("11", ()->{
                if (true==true) throw new RuntimeException("error");
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        jc.scenario("12", ()->{
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();
    }

    @Test
    public void TestRuntimeErrorIn111() {
        ValidateMock validate = new ValidateMock(
                "Feature: 1\n" +
                        "Scenario: 11\n" +
                        "Given 111\n" +
                        "When 112\n" +
                        "Then 113\n" +
                        "Scenario: 12\n" +
                        "Given 121\n" +
                        "When 122\n" +
                        "Then 123\n"
        );
        JC jc = new JC(this, new JCPlugin[] {validate},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                if (true==true) throw new RuntimeException("error");
                jc.when("112");
                jc.then("113");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        jc.scenario("12", ()->{
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();
    }

    @Test
    public void TestRuntimeErrorIn112() {
        ValidateMock validate = new ValidateMock(
                "Feature: 1\n" +
                        "Scenario: 11\n" +
                        "Given 111\n" +
                        "When 112\n" +
                        "Then 113\n" +
                        "Scenario: 12\n" +
                        "Given 121\n" +
                        "When 122\n" +
                        "Then 123\n"
        );
        JC jc = new JC(this, new JCPlugin[] {validate},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                if (true==true) throw new RuntimeException("error");
                jc.then("113");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        jc.scenario("12", ()->{
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();
    }

    @Test
    public void TestRuntimeErrorIn113() {
        ValidateMock validate = new ValidateMock(
                "Feature: 1\n" +
                        "Scenario: 11\n" +
                        "Given 111\n" +
                        "When 112\n" +
                        "Then 113\n" +
                        "Scenario: 12\n" +
                        "Given 121\n" +
                        "When 122\n" +
                        "Then 123\n"
        );
        JC jc = new JC(this, new JCPlugin[] {validate},"1");
        try {
            jc.scenario("11", ()->{
                jc.given("111");
                jc.when("112");
                jc.then("113");
                if (true==true) throw new RuntimeException("error");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        jc.scenario("12", ()->{
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();
    }

    @Test
    public void TestRuntimeErrorIn12() {
        ValidateMock validate = new ValidateMock(
                "Feature: 1\n" +
                        "Scenario: 11\n" +
                        "Given 111\n" +
                        "When 112\n" +
                        "Then 113\n" +
                        "Scenario: 12\n" +
                        "Given 121\n" +
                        "When 122\n" +
                        "Then 123\n"
        );
        JC jc = new JC(this, new JCPlugin[] {validate},"1");
        jc.scenario("11", ()->{
            jc.given("111");
            jc.when("112");
            jc.then("113");
        });
        try {
            jc.scenario("12", ()->{
                if (true==true) throw new RuntimeException("error");
                jc.given("121");
                jc.when("122");
                jc.then("123");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        jc.finished();
    }

    @Test
    public void TestRuntimeErrorIn123() {
        ValidateMock validate = new ValidateMock(
                "Feature: 1\n" +
                        "Scenario: 11\n" +
                        "Given 111\n" +
                        "When 112\n" +
                        "Then 113\n" +
                        "Scenario: 12\n" +
                        "Given 121\n" +
                        "When 122\n" +
                        "Then 123\n"
        );
        JC jc = new JC(this, new JCPlugin[] {validate},"1");
        jc.scenario("11", ()->{
            jc.given("111");
            jc.when("112");
            jc.then("113");
        });
        try {
            jc.scenario("12", ()->{
                jc.given("121");
                jc.when("122");
                jc.then("123");
                if (true==true) throw new RuntimeException("error");
            });
        } catch (JCException ex) {
            Assert.assertEquals("error", ex.getCause().getMessage());
        }
        jc.finished();
    }

}
