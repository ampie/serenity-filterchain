package net.serenitybdd.cucumber.filterchain;

import java.util.*;

public class FilterChainConfig {
    private List<InputLinkConfig> inputs;
    private List<ProcessorLinkConfig> processors;
    private List<OutputLinkConfig> outputs;
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
    public List<OutputLink> buildLinks(){
        for (LinkConfig<?> input :  getConfigs(inputs,processors,outputs)) {
            links.put(input.getName(), input.buildLink());
        }
        for (ProcessorLinkConfig processorLinkConfig : processors) {
            link(processorLinkConfig.getName(),processorLinkConfig.getSources());
        }
        for (OutputLinkConfig output : outputs) {
            link(output.getName(),output.getSources());
        }
        return getOutputLinks();
    }

    public List<OutputLink> getOutputLinks() {
        List<OutputLink> result = new ArrayList<>();
        for (TestOutcomeLink link : links.values()) {
            if(link instanceof OutputLink){
                result.add((OutputLink) link);
            }
        }
        return result;
    }

    private List<LinkConfig> getConfigs(List<? extends LinkConfig> ... configs) {
        List<LinkConfig> result = new ArrayList<>();
        for (List<? extends LinkConfig> config : configs) {
            result.addAll(config);
        }
        return result;
    }
    private void link(String receivingLinkName, Collection<String> inputs){
        ReceivingLink link = (ReceivingLink) links.get(receivingLinkName);
        for (String input : inputs) {
            ProducingLink source = (ProducingLink) links.get(input);
            if(source ==null){
                throw new IllegalArgumentException("Link " + input + " is not registered");
            }
            link.addSource(source);
        }
        links.put(link.getName(),(TestOutcomeLink)link);
    }
}
