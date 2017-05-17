package net.serenitybdd.cucumber.filterchain.inputs;

import net.serenitybdd.cucumber.adapter.CucumberJsonAdapter;
import net.serenitybdd.cucumber.filterchain.InputStrategy;
import net.thucydides.core.model.TestOutcome;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CucumberImporter implements InputStrategy {
    CucumberJsonAdapter cucumberJsonAdapter = new CucumberJsonAdapter();
    @Override
    public List<TestOutcome> extract(File sourceDirectory) {
        try {
            return cucumberJsonAdapter.loadOutcomesFrom(sourceDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
