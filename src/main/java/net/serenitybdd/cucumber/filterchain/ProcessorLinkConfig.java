
package net.serenitybdd.cucumber.filterchain;

import java.util.List;

public class ProcessorLinkConfig extends LinkConfig<ProcessorLink> {

    private List<String> sources;

    public ProcessorLinkConfig() {
    }

    public ProcessorLinkConfig(String name, String implementationClass, List<String> sources) {
        super(name, implementationClass);
        setSources(sources);
    }

    @Override
    public ProcessorLink instantiate() {
        ProcessorLink link = new ProcessorLink();
        return link;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }
}