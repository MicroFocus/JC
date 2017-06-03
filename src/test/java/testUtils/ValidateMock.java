package testUtils;

import com.hpe.jc.plugins.JCPValidateFlowBy;

/**
 * Created by koreny on 6/3/2017.
 */
public class ValidateMock extends JCPValidateFlowBy {
    public ValidateMock(String script) {
        super();
        this.expectedFeature = parseGherkinScript(script);
    }
}
