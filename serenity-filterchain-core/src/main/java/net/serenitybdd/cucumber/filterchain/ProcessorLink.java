package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.util.ArrayList;
import java.util.List;

public class ProcessorLink extends TestOutcomeLink<ProcessingStrategy> implements ReceivingLink, ProducingLink {
    private List<ProducingLink> sources = new ArrayList<>();
    private List<TestOutcome> output;

    public List<ProducingLink> getSources() {
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
