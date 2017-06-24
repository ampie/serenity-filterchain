package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputLink extends TestOutcomeLink<OutputStrategy> implements ReceivingLink {
    private List<TestOutcomeProducingLink> sources = new ArrayList<>();
    private File outputDirectory;

    public List<TestOutcomeProducingLink> getSources() {
        return sources;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void write() {
        outputDirectory.mkdirs();
        getImplementation().writeTo(outputDirectory, retrieveInputOutcomes());
    }

    public void clean() {
        try {
            FileUtils.forceDelete(outputDirectory);
        } catch (IOException e) {
        }
    }

    public List<TestOutcome> retrieveInputOutcomes() {
        List<TestOutcome> inputs = new ArrayList<>();
        for (TestOutcomeProducingLink input : this.sources) {
            inputs.addAll(input.produce());
        }
        return inputs;
    }
    
    @Override
    public void addSource(TestOutcomeLink source) {
        sources.add((TestOutcomeProducingLink) source);
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
