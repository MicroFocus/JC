package unitTests; /**
 * Created by koreny on 3/20/2017.
 */

import com.hpe.jc.JC;
import com.hpe.jc.JCCannotContinueException;
import com.hpe.jc.JCPlugin;
import com.hpe.jc.errors.GherkinAssert;
import com.hpe.jc.errors.JCException;
import com.hpe.jc.plugins.JCPValidateFlowBy;
import org.junit.Assert;
import org.junit.Test;
import testUtils.ValidateMock;

public class JCValidateFlowBackground {

    //region scripts for the tests
    final String script =
            "Feature: 1\n" +
                    "Background: 10\n" +
                    "Given 101\n" +
                    "And 102\n" +
                    "But 103\n" +
                    "Scenario: 11\n" +
                    "Given 111\n" +
                    "When 112\n" +
                    "Then 113\n" +
                    "Scenario: 12\n" +
                    "Given 121\n" +
                    "When 122\n" +
                    "Then 123\n";

    final String scriptWithoutBackground =
            "Feature: 1\n" +
                    "Scenario: 11\n" +
                    "Given 111\n" +
                    "When 112\n" +
                    "Then 113\n" +
                    "Scenario: 12\n" +
                    "Given 121\n" +
                    "When 122\n" +
                    "Then 123\n";

    //endregion

    @Test
    public void TestBasicFlowShouldWork() {
        ValidateMock validate = new ValidateMock(script);
        JC jc = new JC(this, new JCPlugin[]{validate}, "1");

        jc.background("10", () -> {
            jc.given("101");
            jc.and("102");
            jc.but("103");
        });
        jc.scenario("11", () -> {
            jc.given("111");
            jc.when("112");
            jc.then("113");
        });
        jc.background("10", () -> {
            jc.given("101");
            jc.and("102");
            jc.but("103");
        });
        jc.scenario("12", () -> {
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();
    }

    @Test
    public void TestBackgroundIsMissing() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.BACKGROUND_MISSING;
        try {
            ValidateMock validate = new ValidateMock(script);
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");

            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.background("10", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
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
    public void TestBackgroundIsUsedButNotNeededAsItIsNotDeclaredInFeatureFile() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.BACKGROUND_IS_NOT_EXPECTED;
        try {
            ValidateMock validate = new ValidateMock(scriptWithoutBackground);
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");

            jc.background("10", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
            });
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.background("10", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
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
    public void TestBackgroundTitleMismatch() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.BACKGROUND_TITLE_MISMATCH;
        try {
            ValidateMock validate = new ValidateMock(script);
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");

            jc.background("", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
            });
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.background("10", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
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
    public void TestBackgroundStepMismatch() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            ValidateMock validate = new ValidateMock(script);
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");

            jc.background("10", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
            });
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("112");
                jc.then("113");
            });
            jc.background("10", () -> {
                jc.given("101");
                jc.and("error");
                jc.but("103");
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

    @Test(expected = JCException.class)
    public void TestBackgroundThrowsException() {

        ValidateMock validate = new ValidateMock(script);
        JC jc = new JC(this, new JCPlugin[]{validate}, "1");

        jc.background("10", () -> {
            jc.given("101");
            if (true==true) throw new RuntimeException("oops!");
            jc.and("102");
            jc.but("103");
        });

        jc.scenario("11", () -> {
            jc.given("111");
            jc.when("112");
            jc.then("113");
        });
        jc.background("10", () -> {
            jc.given("101");
            jc.and("102");
            jc.but("103");
        });
        jc.scenario("12", () -> {
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();
    }

    @Test
    public void TestScenarioAfterBackgroundIsMissing() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.BACKGROUND_MISSING;
        try {
            ValidateMock validate = new ValidateMock(script);
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");

            jc.background("10", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
            });
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

    @Test(expected = JCException.class)
    public void TestScenarioAfterBackgroundThrowException() {

        ValidateMock validate = new ValidateMock(script);
        JC jc = new JC(this, new JCPlugin[]{validate}, "1");

        jc.background("10", () -> {
            jc.given("101");
            jc.and("102");
            jc.but("103");
        });

        jc.scenario("11", () -> {
            jc.given("111");
            if (true==true) throw new RuntimeException("oops!");
            jc.when("112");
            jc.then("113");
        });

        jc.background("10", () -> {
            jc.given("101");
            jc.and("102");
            jc.but("103");
        });

        jc.scenario("12", () -> {
            jc.given("121");
            jc.when("122");
            jc.then("123");
        });
        jc.finished();
    }

    @Test
    public void TestScenarioAfterBackgroundStepMismatch() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            ValidateMock validate = new ValidateMock(script);
            JC jc = new JC(this, new JCPlugin[]{validate}, "1");

            jc.background("10", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
            });
            jc.scenario("11", () -> {
                jc.given("111");
                jc.when("error");
                jc.then("113");
            });
            jc.background("10", () -> {
                jc.given("101");
                jc.and("102");
                jc.but("103");
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


}
