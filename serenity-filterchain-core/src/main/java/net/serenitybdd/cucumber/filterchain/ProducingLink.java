package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.util.List;

public interface ProducingLink {
    List<TestOutcome> produce();

    String getName();
}
