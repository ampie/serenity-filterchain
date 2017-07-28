package net.serenitybdd.cucumber.filterchain.integrators;

import net.serenitybdd.cucumber.filterchain.ProcessingStrategy;
import net.serenitybdd.plugins.jira.JiraUpdater;
import net.serenitybdd.plugins.jira.TestResultTally;
import net.serenitybdd.plugins.jira.model.IssueTracker;
import net.serenitybdd.plugins.jira.workflow.WorkflowLoader;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestOutcomeSummary;
import net.thucydides.core.util.EnvironmentVariables;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Now wouldn't that be nice!!!
 * WEBDAV may be the approach to go
 */
public class ConfluenceUpdatingProcessor implements ProcessingStrategy {


    public ConfluenceUpdatingProcessor() {

    }

    @Override
    public List<TestOutcome> process(List<TestOutcome> source) {
        return source;
    }
}
