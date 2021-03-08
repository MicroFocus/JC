package testUtils;

import com.microfocus.jc.plugins.JCPFeatureFileValidator;

/**
 * Created by koreny on 6/3/2017.
 */
public class ValidateMock extends JCPFeatureFileValidator {
    public ValidateMock(String script) {
        super();
        this.expectedFeature = parseGherkinScript(script);
    }
}
