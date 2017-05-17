package net.serenitybdd.cucumber.filterchain.outputs;

import net.serenitybdd.cucumber.filterchain.OutputStrategy;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.reports.AcceptanceTestReporter;
import net.thucydides.core.reports.OutcomeFormat;
import net.thucydides.core.reports.html.HtmlAcceptanceTestReporter;
import net.thucydides.core.reports.json.JSONTestOutcomeReporter;
import net.thucydides.core.reports.xml.XMLTestOutcomeReporter;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class StandardSerenityOutput implements OutputStrategy {
    private OutcomeFormat format = OutcomeFormat.HTML;

    @Override
    public void writeTo(File outputDir, List<TestOutcome> outcomes) {
        AcceptanceTestReporter reporter = getReporter();
        reporter.setOutputDirectory(outputDir);
        try {
        for (TestOutcome outcome : outcomes) {
                reporter.generateReportFor(outcome);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected AcceptanceTestReporter getReporter() {
        switch (format) {
            case HTML:
                return new HtmlAcceptanceTestReporter();
            case XML:
                return new JSONTestOutcomeReporter();
            case JSON:
                return new XMLTestOutcomeReporter();
            default:
                return new HtmlAcceptanceTestReporter();

        }
    }

    public OutcomeFormat getFormat() {
        return format;
    }

    public void setFormat(OutcomeFormat format) {
        this.format = format;
    }
}
