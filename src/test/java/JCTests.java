/**
 * Created by koreny on 3/20/2017.
 */
import com.hpe.*;
import gherkin.lexer.En;
import gherkin.lexer.Lexer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.ByteArrayOutputStream;

public class JCTests {

    @Test
    public void TestGherkinParser() {
        GherkinLexerListener listener = new GherkinLexerListener();
        Lexer lexer = new En(listener);
        lexer.scan("JC: hello \n" +
                " Scenario: world \n" +
                " Given I do \n" +
                " Then I will");
        Assert.assertEquals(listener.currentFeature.getDescription(), "hello");
        Assert.assertEquals(listener.currentFeature.scenarios.get(0).getDescription(), "world");
        Assert.assertEquals(listener.currentFeature.scenarios.get(0).steps.get(0).getDescription(), "I do");
        Assert.assertEquals(listener.currentFeature.scenarios.get(0).steps.get(0).type, "given");
        Assert.assertEquals(listener.currentFeature.scenarios.get(0).steps.get(1).getDescription(), "I will");
        Assert.assertEquals(listener.currentFeature.scenarios.get(0).steps.get(1).type, "then");
    }


    @Test
    public void happyPathRunningATestWithFeatureDefinition() {

    }


    JC runit(final Class className) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            JUnitCore junit = new JUnitCore();
            Result result = junit.run(className);

        } catch (Exception e) {
            return null;
            //throw new Exception("failed initializze runner", e);
        }

        return null;
    }
}
