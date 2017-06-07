package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.util.List;

public interface ProcessingStrategy {
    List<TestOutcome> process(List<TestOutcome> source);
}
