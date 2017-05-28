package com.hpe.jc.plugins.OctaneFormatter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GherkinSerializer {
    static final String FEATURE_TAG_NAME = "feature";
    static final String RESULTS_FILE_NAME = "OctaneGherkinResults.xml";
    static final String ROOT_TAG_NAME = "features";
    static final String SCENARIO_TAG_NAME = "scenario";
    static final String SCENARIOS_TAG_NAME = "scenarios";
    static final String FILE_TAG_NAME = "file";
    static final String STEP_TAG_NAME = "step";
    static final String STEPS_TAG_NAME = "steps";
    static final String BACKGROUND_TAG_NAME = "background";
    static final String ERROR_MESSAGE_TAG_NAME = "error_message";
    static final String XML_VERSION = "1";
    static final String errorPrefix = "<HPEAlmOctaneGherkinFormatter Error>";
}
