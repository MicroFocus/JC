package unitTests;

/**
 * Created by koreny on 3/20/2017.
 */

import static com.microfocus.jc.JC.*;

import com.microfocus.jc.JCCannotContinueException;
import com.microfocus.jc.JCPlugin;
import com.microfocus.jc.errors.GherkinAssert;
import com.microfocus.jc.errors.JCException;
import com.microfocus.jc.plugins.Feature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testUtils.ValidateMock;

@SuppressWarnings("ALL")
@Feature("1")
public class JCValidateFlowBackground {

    //region scripts for the tests

    ValidateMock validate = null;
            
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

    @Before
    public void Before() {
        validate = new ValidateMock(script);
        reset(new JCPlugin [] {validate});
    }
    
    //endregion

    @Test
    public void TestBasicFlowShouldWork() {

        background(() -> {
            given("101");
            and("102");
            but("103");
        });
        scenario("11", () -> {
            given("111");
            when("112");
            then("113");
        });
        background(() -> {
            given("101");
            and("102");
            but("103");
        });
        scenario("12", () -> {
            given("121");
            when("122");
            then("123");
        });
        finished();
    }

    @Test
    public void TestBackgroundIsMissing() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            scenario("11", () -> {
                given("111");
                when("112");
                then("113");
            });
            background(() -> {
                given("101");
                and("102");
                but("103");
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
    public void TestBackgroundIsUsedButNotNeededAsItIsNotDeclaredInFeatureFile() {
        validate = new ValidateMock("Feature: 1\n" +
                "Scenario: 11\n" +
                "Given 111\n" +
                "When 112\n" +
                "Then 113\n" +
                "Scenario: 12\n" +
                "Given 121\n" +
                "When 122\n" +
                "Then 123\n");
        reset(new JCPlugin [] {validate});

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            background(() -> {
                given("101");
                and("102");
                but("103");
            });
            scenario("11", () -> {
                given("111");
                when("112");
                then("113");
            });
            background(() -> {
                given("101");
                and("102");
                but("103");
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
    public void TestBackgroundStepMismatch() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            background(() -> {
                given("101");
                and("error");
                but("103");
            });
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

    @Test(expected = JCException.class)
    public void TestBackgroundThrowsException() {
        background(() -> {
            given("101");
            if (true==true) throw new RuntimeException("oops!");
            and("102");
            but("103");
        });

        scenario("11", () -> {
            given("111");
            when("112");
            then("113");
        });
        background(() -> {
            given("101");
            and("102");
            but("103");
        });
        scenario("12", () -> {
            given("121");
            when("122");
            then("123");
        });
        finished();
    }

    @Test
    public void TestBackgroundCanBeDeclaredOnlyOnce() {
            background(() -> {
                given("101");
                and("102");
                but("103");
            });
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

    }

    @Test(expected = JCException.class)
    public void TestScenarioAfterBackgroundThrowException() {
        background(() -> {
            given("101");
            and("102");
            but("103");
        });

        scenario("11", () -> {
            given("111");
            if (true==true) throw new RuntimeException("oops!");
            when("112");
            then("113");
        });

        background(() -> {
            given("101");
            and("102");
            but("103");
        });

        scenario("12", () -> {
            given("121");
            when("122");
            then("123");
        });
        finished();
    }

    @Test
    public void TestScenarioAfterBackgroundStepMismatch() {

        GherkinAssert.ERROR_TYPES expectedId = GherkinAssert.ERROR_TYPES.STEP_MISMATCH;
        try {
            background(() -> {
                given("101");
                and("102");
                but("103");
            });
            scenario("11", () -> {
                given("111");
                when("error");
                then("113");
            });
            background(() -> {
                given("101");
                and("102");
                but("103");
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


}
