package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.io.File;
import java.util.List;

/**
 * Suppor Serenity  JSON, HTML and XML
 */
public interface OutputStrategy {
    void writeTo(File outputDir, List<TestOutcome> outcomes);
}
