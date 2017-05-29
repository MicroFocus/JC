package com.hpe.jc.plugins.OctaneFormatter;

import com.hpe.jc.JCPlugin;
import com.hpe.jc.errors.JCException;
import com.hpe.jc.gherkin.GherkinFeature;
import com.hpe.jc.gherkin.GherkinScenario;
import com.hpe.jc.gherkin.GherkinStep;
import com.hpe.jc.plugins.JCPValidateFlowBy;
import com.hpe.jc.plugins.JCTimePlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.FileOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.hpe.jc.plugins.OctaneFormatter.GherkinSerializer.*;

/**
 * Created by koreny on 4/2/2017.
 */
public class JCOctaneCucumberFormatter extends JCPlugin {

    @Override
    protected void onInit() { }

    @Override
    protected void onEndOfAny() {

    }

    @Override
    protected void onStartOfAny() {

    }

    @Override
    protected void onFeatureStart() {

    }

    @Override
    protected void onFeatureEnd() {
        writeXMLResult();
    }

    @Override
    protected void onScenarioStart() {

    }

    @Override
    protected void onScenarioEnd() {

    }

    @Override
    protected void onStepStart() {

    }

    @Override
    protected void onStepEnd() {

    }

    @Override
    protected void onStepFailure(Throwable ex) {

    }

    private static String getOctaneTag(GherkinFeature feature) {
        for (String tag : feature.tags) {
            if (tag.startsWith("@TID")) return tag;
        }

        return "";
    }

    private HashMap<GherkinScenario, GherkinScenario> scenarioMap;
    private HashMap<GherkinStep, GherkinStep> stepMap;

    public void writeXMLResult() {
        // generate empty document
        Document doc = null;
        Element rootElement = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            rootElement = doc.createElement(GherkinSerializer.ROOT_TAG_NAME);
            rootElement.setAttribute("version",XML_VERSION);
            doc.appendChild(rootElement);
        } catch (ParserConfigurationException e) {
            throw new JCException(errorPrefix + "Failed to create xml document",e);
        }

        GherkinFeature expectedFeature = JCPValidateFlowBy.getExpectedFeature(progress.getCurrentFeature());
        scenarioMap = JCPValidateFlowBy.getExpectedToActualScenarioMap(progress.getCurrentFeature());
        stepMap = JCPValidateFlowBy.getExpectedToActualStepMap(progress.getCurrentFeature());

        boolean isDefFileMissing = expectedFeature == null || scenarioMap==null || stepMap==null;
        if (isDefFileMissing) {
            throw new JCException(String.format("%s plugin is missing. It is needed to generate an octane XML result", JCPValidateFlowBy.class.toString()));
        }

        // generate report
        rootElement.appendChild(getXMLForFeature(expectedFeature, progress.getCurrentFeature(), doc));

        // write it to disk
        try {
            DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) reg.getDOMImplementation("LS");
            LSSerializer serializer = impl.createLSSerializer();
            LSOutput output = impl.createLSOutput();
            FileOutputStream out = new FileOutputStream(GherkinSerializer.RESULTS_FILE_NAME);
            output.setByteStream(out);
            serializer.write(doc, output);
        } catch (Exception e) {
            throw new JCException(errorPrefix + "Failed to write document to disc",e);
        }
    }


    public Element getXMLForFeature(GherkinFeature expectedFeature, GherkinFeature actualFeature, Document doc) {

        Element feature = doc.createElement(GherkinSerializer.FEATURE_TAG_NAME);

        // Adding the feature members
        feature.setAttribute("name", expectedFeature.getDescription());
        feature.setAttribute("path", JCPValidateFlowBy.getFeatureFileLocation(actualFeature));
        feature.setAttribute("tag", getOctaneTag(expectedFeature));

        // get start date/time
        Date startTime = JCTimePlugin.getStartTime(actualFeature);
        feature.setAttribute("started", String.valueOf(startTime.getTime()));
        //Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        //feature.setAttribute("started",  format.format(startTime));

        // Adding the file to the feature
        Element fileElement = doc.createElement(GherkinSerializer.FILE_TAG_NAME);
        String expectedScript = JCPValidateFlowBy.getExpectedScript(actualFeature);
        fileElement.appendChild(doc.createCDATASection(expectedScript));
        feature.appendChild(fileElement);

        Element scenariosElement = doc.createElement(GherkinSerializer.SCENARIOS_TAG_NAME);

        // Serializing the background
        /* NOT SUPPORTED YET
        if (_backgroundSteps != null && _backgroundSteps.size()>0) {
            Element backgroundStepsElement = doc.createElement(GherkinSerializer.STEPS_TAG_NAME);
            for (StepElement step : _backgroundSteps) {
                backgroundStepsElement.appendChild(step.toXMLElement(doc));
            }

            Element backgroundElement = doc.createElement(GherkinSerializer.BACKGROUND_TAG_NAME);
            backgroundElement.appendChild(backgroundStepsElement);
            scenariosElement.appendChild(backgroundElement);
        }
        */

        // Serializing the scenarios
        for (GherkinScenario scenario : expectedFeature.scenarios) {
            scenariosElement.appendChild(getXMLForScenario(scenario, doc));
        }

        feature.appendChild(scenariosElement);

        return feature;
    }

    public Element getXMLForScenario(GherkinScenario jcscenario, Document doc) {
        // Adding the feature members
        Element scenario = doc.createElement(SCENARIO_TAG_NAME);
        scenario.setAttribute("name", jcscenario.getDescription());
        /*** scenario outline - not supported yet
        if(_outlineIndex>0){
            scenario.setAttribute("outlineIndex", _outlineIndex.toString());
        }
        */
        // Serializing the steps
        Element steps = doc.createElement(STEPS_TAG_NAME);
        for (GherkinStep jcstep : jcscenario.steps) {
            steps.appendChild(getXMLForStep(jcstep, stepMap.get(jcstep), doc));
        }

        scenario.appendChild(steps);

        return scenario;
    }


    public Element getXMLForStep(GherkinStep expectedStep, GherkinStep actualStep, Document doc) {

        Element step = doc.createElement(STEP_TAG_NAME);

        String errorMessages = "";

        if (actualStep == null) {
            step.setAttribute("name", expectedStep.getDescription());
            step.setAttribute("status", "skipped");
            step.setAttribute("duration", "0");

            errorMessages += String.format("step was skipped due to error in previous step");

        } else {
            Date startTime = JCTimePlugin.getStartTime(actualStep);
            Date endTime = JCTimePlugin.getEndTime(actualStep);
            long duration = (endTime.getTime() - startTime.getTime()); // in sec

            int numOfErrors = actualStep.getFatalExceptions().size()+actualStep.getPluginExceptions().size()+actualStep.getTestExceptions().size();
            step.setAttribute("name", expectedStep.getDescription());
            step.setAttribute("status", numOfErrors > 0 ? "failed" : "passed");
            step.setAttribute("duration", String.valueOf(duration));
            for (Throwable ex : actualStep.getTestExceptions()) {
                errorMessages += ex.getMessage() + "\n\n";
            }
            for (Throwable ex : actualStep.getFatalExceptions()) {
                errorMessages += ex.getMessage() + "\n\n";
            }
            for (Throwable ex : actualStep.getPluginExceptions()) {
                errorMessages += ex.getMessage() + "\n\n";
            }
        }

       if(!errorMessages.isEmpty()){
            Element errorElement = doc.createElement(GherkinSerializer.ERROR_MESSAGE_TAG_NAME);
            errorElement.appendChild(doc.createCDATASection(errorMessages));
            step.appendChild(errorElement);
        }

        return step;
    }
}
