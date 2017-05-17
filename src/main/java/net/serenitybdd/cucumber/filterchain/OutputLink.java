package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputLink extends TestOutcomeLink<OutputStrategy> implements ReceivingLink {
    private List<ProducingLink> sources = new ArrayList<>();
    private File outputDirectory;

    public List<ProducingLink> getSources() {
        return sources;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void write() {
        outputDirectory.mkdirs();
        getImplementation().writeTo(outputDirectory, retrieveInputOutcomes());
    }

    public List<TestOutcome> retrieveInputOutcomes() {
        List<TestOutcome> inputs = new ArrayList<>();
        for (ProducingLink input : this.sources) {
            inputs.addAll(input.produce());
        }
        return inputs;
    }
    
    @Override
    public void addSource(ProducingLink source) {
        sources.add(source);
    }
}
