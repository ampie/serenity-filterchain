package net.serenitybdd.cucumber.filterchain;

import net.serenitybdd.cucumber.filterchain.InputStrategy;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DummyInputStrategy implements InputStrategy {
    @Override
    public List<TestOutcome> extract(File sourceDirectory) {
        return Arrays.asList(TestOutcome.forTestInStory("My Scenario", Story.called("My Story")));
    }
}
