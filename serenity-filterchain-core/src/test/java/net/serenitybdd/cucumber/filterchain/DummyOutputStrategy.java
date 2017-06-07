package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.io.File;
import java.util.List;

public class DummyOutputStrategy implements OutputStrategy {
    @Override
    public void writeTo(File outputDir, List<TestOutcome> outcomes) {

    }
}
