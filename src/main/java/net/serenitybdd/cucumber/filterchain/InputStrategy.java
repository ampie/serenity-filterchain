package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.io.File;
import java.util.List;

public interface InputStrategy {
    List<TestOutcome> extract(File sourceDirectory);
}
