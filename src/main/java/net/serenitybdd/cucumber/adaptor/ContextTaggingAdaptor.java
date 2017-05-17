package net.serenitybdd.cucumber.adaptor;

import com.google.common.collect.Lists;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;
import net.thucydides.core.reports.TestOutcomeStream;
import net.thucydides.core.reports.adaptors.ExtendedTestOutcomeAdaptor;
import net.thucydides.core.reports.adaptors.common.FilebasedOutcomeAdaptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ContextTaggingAdaptor extends FilebasedOutcomeAdaptor implements ExtendedTestOutcomeAdaptor {
    private Contextualizer contextualizer = new Contextualizer();

    @Override
    public List<TestOutcome> loadOutcomesFrom(File sourceDir) throws IOException {
        List<TestOutcome> testOutcomes = Lists.newArrayList();
        Path directory = Paths.get(sourceDir.getAbsolutePath());
        try (TestOutcomeStream stream = TestOutcomeStream.testOutcomesInDirectory(directory)) {
            for (TestOutcome outcome : stream) {
                if (shouldRetain(outcome)) {
                    TestOutcome contextualizedOutcome = contextualizer.contextualize(outcome);
                    testOutcomes.add(contextualizedOutcome);
                    /**
                     * TODO debug this
                     */
                    if (false) {
                        if (this.contextualizer.getContext() != null) {
                            for (TestTag tag : outcome.getTags()) {
                                if (tag.getName().startsWith(this.contextualizer.getContext())) {
                                    String[] split = tag.getName().split("_");
                                    if (split.length > 2) {
                                        contextualizedOutcome.addIssues(Arrays.asList(split[2]));
                                    }

                                }
                            }
                            //TODO get issue number from story/feature/requirement
                        }
                    }

                }
            }
        }
        return testOutcomes;
    }

    private boolean shouldRetain(TestOutcome outcome) {
        return outcome.getUserStory() != null && !"scope".equals(outcome.getUserStory().getType()) && !"undefined".equals(outcome.getUserStory().getType()) && outcome.getUserStory().getType() != null;
    }

    @Override
    public void setSourceContext(String sourceContext) {
        contextualizer.setContext(sourceContext);
    }

    @Override
    public void setScenarioStatus(String scenarioStatus) {
        contextualizer.setScenarioStatus(scenarioStatus);

    }

    @Override
    public void copySupportingResourcesTo(List<TestOutcome> outcomes, File targetDirectory) throws IOException {

    }
}
