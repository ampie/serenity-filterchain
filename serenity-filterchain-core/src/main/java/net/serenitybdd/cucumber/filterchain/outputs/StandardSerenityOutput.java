package net.serenitybdd.cucumber.filterchain.outputs;

import net.serenitybdd.cucumber.filterchain.OutputStrategy;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.reports.AcceptanceTestReporter;
import net.thucydides.core.reports.OutcomeFormat;
import net.thucydides.core.reports.html.HtmlAcceptanceTestReporter;
import net.thucydides.core.reports.json.JSONTestOutcomeReporter;
import net.thucydides.core.reports.xml.XMLTestOutcomeReporter;
import net.thucydides.core.screenshots.ScreenshotAndHtmlSource;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class StandardSerenityOutput implements OutputStrategy {
    private static final String DEFAULT_SOURCE_DIR = "target/site/serenity";
    private OutcomeFormat format = OutcomeFormat.HTML;

    @Override
    public void writeTo(File myOutputDirectory, List<TestOutcome> outcomes) {
        EnvironmentVariables variables =new SystemEnvironmentVariables();
        AcceptanceTestReporter reporter = getReporter();
        reporter.setOutputDirectory(myOutputDirectory);
        String defaultOutputDirName = ThucydidesSystemProperty.THUCYDIDES_OUTPUT_DIRECTORY.from(variables);
        if(defaultOutputDirName == null){
            defaultOutputDirName=DEFAULT_SOURCE_DIR;
        }
        File defaultOutputDirectory= new File(defaultOutputDirName);
        try {
            for (TestOutcome outcome : outcomes) {
                reporter.generateReportFor(outcome);
                for (ScreenshotAndHtmlSource screenshot : outcome.getScreenshotAndHtmlSources()) {
                    File targetFile = screenshot.getScreenshotFile(myOutputDirectory);
                    File sourceFile = screenshot.getScreenshotFile(defaultOutputDirectory);
                    if(!targetFile.exists() &&  sourceFile.exists()){
                        FileUtils.copyFile(sourceFile, targetFile);
                    }
                    File targetScaledFile = new File(myOutputDirectory, "scaled_" + screenshot.getScreenshot().getName());
                    File sourceScaledFile = new File(defaultOutputDirectory, "scaled_" + screenshot.getScreenshot().getName());
                    if(!targetScaledFile.exists() &&  sourceScaledFile.exists()){
                        FileUtils.copyFile(sourceScaledFile, targetScaledFile);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected AcceptanceTestReporter getReporter() {
        switch (format) {
            case HTML:
                return new HtmlAcceptanceTestReporter(){
                    @Override
                    protected void copyResourcesToOutputDirectory() throws IOException {
//                        super.copyResourcesToOutputDirectory();
                    }
                };
            case XML:
                return new JSONTestOutcomeReporter();
            case JSON:
                return new XMLTestOutcomeReporter();
            default:
                return new HtmlAcceptanceTestReporter(){
                    @Override
                    protected void copyResourcesToOutputDirectory() throws IOException {
//                        super.copyResourcesToOutputDirectory();
                    }
                };

        }
    }

    public OutcomeFormat getFormat() {
        return format;
    }

    public void setFormat(OutcomeFormat format) {
        this.format = format;
    }
}
