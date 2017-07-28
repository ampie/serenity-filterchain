package net.serenitybdd.cucumber.filterchain;

import java.util.*;

public class FilterChainConfig {
    private List<InputLinkConfig> inputs;
    private List<ProcessorLinkConfig> processors;
    private List<OutputLinkConfig> outputs;
    private List<PublishingLinkConfig> publishers;
    private Map<String, TestOutcomeLink> links = new HashMap<>();

    public List<InputLinkConfig> getInputs() {
        return inputs;
    }

    public void setInputs(List<InputLinkConfig> inputs) {
        this.inputs = inputs;
    }

    public List<ProcessorLinkConfig> getProcessors() {
        return processors;
    }

    public void setProcessors(List<ProcessorLinkConfig> processors) {
        this.processors = processors;
    }

    public List<OutputLinkConfig> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OutputLinkConfig> outputs) {
        this.outputs = outputs;
    }

    public List<OutputLink> buildLinks() {
        if(inputs == null || inputs.isEmpty()){
            throw new IllegalStateException("No inputs configured!");
        }
        if(outputs == null || outputs.isEmpty()){
            throw new IllegalStateException("No outputs configured!");
        }
        for (LinkConfig<?> input : getConfigs(inputs, processors, outputs, publishers)) {
            links.put(input.getName(), input.buildLink());
        }
        if (processors != null) {
            for (ProcessorLinkConfig processorLinkConfig : processors) {
                link(processorLinkConfig.getName(), processorLinkConfig.getSources());
            }
        }
        for (OutputLinkConfig output : outputs) {
            link(output.getName(), output.getSources());
        }
        if (publishers != null) {
            for (PublishingLinkConfig publisher : publishers) {
                link(publisher.getName(), publisher.getSources());
            }
        }
        return getOutputLinks();
    }

    public List<OutputLink> getOutputLinks() {
        List<OutputLink> result = new ArrayList<>();
        for (TestOutcomeLink link : links.values()) {
            if (link instanceof OutputLink) {
                result.add((OutputLink) link);
            }
        }
        return result;
    }

    public List<PublishingLink> getPublishingLinks() {
        List<PublishingLink> result = new ArrayList<>();
        for (TestOutcomeLink link : links.values()) {
            if (link instanceof PublishingLink) {
                result.add((PublishingLink) link);
            }
        }
        return result;
    }

    private List<LinkConfig> getConfigs(List<? extends LinkConfig>... configs) {
        List<LinkConfig> result = new ArrayList<>();
        for (List<? extends LinkConfig> config : configs) {
            if (config != null) {
                result.addAll(config);
            }
        }
        return result;
    }

    private void link(String receivingLinkName, Collection<String> inputs) {
        ReceivingLink link = (ReceivingLink) links.get(receivingLinkName);
        for (String input : inputs) {
            TestOutcomeLink source = links.get(input);
            if (source == null) {
                throw new IllegalArgumentException("Link " + input + " is not registered");
            }
            link.addSource(source);
        }
        links.put(link.getName(), (TestOutcomeLink) link);
    }

    public List<PublishingLinkConfig> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<PublishingLinkConfig> publishers) {
        this.publishers = publishers;
    }
}
