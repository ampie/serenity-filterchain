package net.serenitybdd.cucumber.filterchain;

import java.util.ArrayList;
import java.util.List;

public class PublishingLink extends TestOutcomeLink<PublishingStrategy> implements ReceivingLink {
    private List<OutputLink> sources = new ArrayList<>();
    private String destination;

    public List<OutputLink> getSources() {
        return sources;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void publish() {
        for (OutputLink source : sources) {
            getImplementation().copy(source.getOutputDirectory(), destination);
        }
    }

    @Override
    public void addSource(TestOutcomeLink source) {
        sources.add((OutputLink) source);
    }
}
