package net.serenitybdd.cucumber.filterchain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PublishingLink extends TestOutcomeLink<PublishingStrategy> implements ReceivingLink {
    private List<OutputLink> sources = new ArrayList<>();
    private List<File> sourceDirectories;
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
        for (File source : sourceDirectories) {
            getImplementation().copy(source, destination);
        }
    }

    @Override
    public void addSource(TestOutcomeLink source) {
        sources.add((OutputLink) source);
    }

    public void setSourceDirectories(List<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }
}
