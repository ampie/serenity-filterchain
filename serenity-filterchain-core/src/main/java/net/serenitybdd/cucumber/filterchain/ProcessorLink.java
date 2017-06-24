package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.util.ArrayList;
import java.util.List;

public class ProcessorLink extends TestOutcomeLink<ProcessingStrategy> implements ReceivingLink, TestOutcomeProducingLink {
    private List<TestOutcomeProducingLink> sources = new ArrayList<>();
    private List<TestOutcome> output;

    public List<TestOutcomeProducingLink> getSources() {
        return sources;
    }

    @Override
    public List<TestOutcome> produce() {
        if (this.output == null) {
            this.output = getImplementation().process(retrieveInputOutcomes());
        }
        return this.output;
    }

    private List<TestOutcome> retrieveInputOutcomes() {
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
}
