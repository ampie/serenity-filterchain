package net.serenitybdd.cucumber.filterchain.inputs;

import net.serenitybdd.cucumber.filterchain.InputStrategy;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.reports.OutcomeFormat;
import net.thucydides.core.reports.TestOutcomeLoader;

import java.io.File;
import java.util.List;

public class SerenityXmlImporter implements InputStrategy {
    private TestOutcomeLoader jsonAdapter = new TestOutcomeLoader().forFormat(OutcomeFormat.XML);

    @Override
    public List<TestOutcome> extract(File sourceDirectory) {
        return jsonAdapter.loadFrom(sourceDirectory);
    }
}
