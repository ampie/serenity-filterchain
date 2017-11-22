package net.serenitybdd.cucumber.filterchain.integrators;

import net.serenitybdd.cucumber.filterchain.ProcessingStrategy;
import net.serenitybdd.plugins.jira.JiraUpdater;
import net.serenitybdd.plugins.jira.TestResultTally;
import net.serenitybdd.plugins.jira.model.IssueTracker;
import net.serenitybdd.plugins.jira.service.JiraIssueTracker;
import net.serenitybdd.plugins.jira.service.SystemPropertiesJIRAConfiguration;
import net.serenitybdd.plugins.jira.workflow.ClasspathWorkflowLoader;
import net.serenitybdd.plugins.jira.workflow.WorkflowLoader;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestOutcomeSummary;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JiraUpdatingProcessor implements ProcessingStrategy {

    private final JiraUpdater jiraUpdater;

    private JiraUpdatingProcessor(IssueTracker issueTracker,
                                  EnvironmentVariables environmentVariables,
                                  WorkflowLoader loader) {
        jiraUpdater = new JiraUpdater(issueTracker, environmentVariables, loader);
    }

//    public JiraUpdatingProcessor() {
//        this(Injectors.getInjector().getInstance(IssueTracker.class),
//                Injectors.getInjector().getProvider(EnvironmentVariables.class).get(),
//                Injectors.getInjector().getInstance(WorkflowLoader.class));
//
//    }


    public JiraUpdatingProcessor() {
        this(new JiraIssueTracker(new SystemPropertiesJIRAConfiguration()),new SystemEnvironmentVariables(),new ClasspathWorkflowLoader("jira.workflow",new SystemEnvironmentVariables()));
    }

    private void loadTestOutcomeSummary(Set<String> allIssues, TestOutcomeSummary outcomeSummary, TestResultTally<TestOutcomeSummary> resultTally) {
        for (String issue : jiraUpdater.getPrefixedIssuesWithoutHashes(outcomeSummary)) {
            resultTally.recordResult(issue, outcomeSummary);
        }
        allIssues.addAll(jiraUpdater.getPrefixedIssuesWithoutHashes(outcomeSummary));
    }

    @Override
    public List<TestOutcome> process(List<TestOutcome> source) {
        System.out.println("jiraUpdater.shouldUpdateIssues():" + jiraUpdater.shouldUpdateIssues());
        if (jiraUpdater.shouldUpdateIssues()) {
            TestResultTally<TestOutcomeSummary> resultTally = new TestResultTally<TestOutcomeSummary>();
            Set<String> allIssues = new HashSet<>();
            for (TestOutcome currentTestOutcome : source) {
                loadTestOutcomeSummary(allIssues, new TestOutcomeSummary(currentTestOutcome), resultTally);
            }
            jiraUpdater.updateIssueStatus(allIssues, resultTally);
        }
        return source;
    }
}
