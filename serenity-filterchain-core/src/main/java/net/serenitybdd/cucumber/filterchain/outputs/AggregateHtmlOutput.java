package net.serenitybdd.cucumber.filterchain.outputs;

import net.serenitybdd.cucumber.filterchain.OutputStrategy;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.reports.TestOutcomes;
import net.thucydides.core.reports.html.HtmlAggregateStoryReporter;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class AggregateHtmlOutput implements OutputStrategy {
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public void writeTo(File outputDir, List<TestOutcome> outcomes) {
        HtmlAggregateStoryReporter reporter = new HtmlAggregateStoryReporter(projectName);
        reporter.setOutputDirectory(outputDir);
        try {
            reporter.generateReportsForTestResultsIn(TestOutcomes.of(outcomes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
