package net.serenitybdd.cucumber.filterchain;

import java.util.Collection;
import java.util.List;

public class PublishingLinkConfig extends LinkConfig<PublishingLink> {
    private String destination;
    private List<String> sources;

    public PublishingLinkConfig() {
    }

    public PublishingLinkConfig(String name, String implementationClass, String destination, List<String> sources) {
        super(name, implementationClass);
        this.destination = destination;
        this.sources = sources;
    }

    @Override
    public PublishingLink instantiate() {
        PublishingLink link = new PublishingLink();
        link.setDestination(getDestination());
        return link;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }
}
