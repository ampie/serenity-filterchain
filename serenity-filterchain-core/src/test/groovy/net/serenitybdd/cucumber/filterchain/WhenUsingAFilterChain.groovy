package net.serenitybdd.cucumber.filterchain

import spock.lang.Specification

import java.nio.file.Paths

class WhenUsingAFilterChain extends Specification {
    def "should configure and link all links"() {
        given:
        def filterChainConfig = new FilterChainConfig();

        def inputLinkConfig = new InputLinkConfig("input1", DummyInputStrategy.class.getName(), Paths.get("/does/not/exits"))
        filterChainConfig.setInputs(Arrays.asList(inputLinkConfig))

        def processorLinkConfig = new ProcessorLinkConfig("processor1", DummyProcessingStrategy.class.getName(), Arrays.asList("input1"))
        filterChainConfig.setProcessors(Arrays.asList(processorLinkConfig))

        def outputLinkConfig = new OutputLinkConfig("output1", DummyOutputStrategy.class.getName(), new File("/does/not/exsit/either"), Arrays.asList("processor1"))
        filterChainConfig.setOutputs(Arrays.asList(outputLinkConfig))
        when:

        def outputs = filterChainConfig.buildLinks();

        then:
        outputs.size() == 1
        outputs[0].sources.size() == 1
        ((ProcessorLink)outputs[0].sources[0]).sources.size() == 1
        outputs[0].retrieveInputOutcomes()[0].title == "My Scenario Processed"
    }
    def "should apply custom properties on strategies"() {
        given:
        def filterChainConfig = new FilterChainConfig();

        def inputLinkConfig = new InputLinkConfig("input1", DummyInputStrategy.class.getName(), Paths.get("/does/not/exits"))
        filterChainConfig.setInputs(Arrays.asList(inputLinkConfig))

        def processorLinkConfig = new ProcessorLinkConfig("processor1", DummyProcessingStrategy.class.getName(), Arrays.asList("input1"))
        processorLinkConfig.setProperties(new Properties());
        processorLinkConfig.getProperties().setProperty("suffix", " My Suffix")
        filterChainConfig.setProcessors(Arrays.asList(processorLinkConfig))

        def outputLinkConfig = new OutputLinkConfig("output1", DummyOutputStrategy.class.getName(), new File("/does/not/exsit/either"), Arrays.asList("processor1"))
        filterChainConfig.setOutputs(Arrays.asList(outputLinkConfig))
        when:

        def outputs = filterChainConfig.buildLinks();

        then:
        outputs.size() == 1
        outputs[0].sources.size() == 1
        ((ProcessorLink)outputs[0].sources[0]).sources.size() == 1
        outputs[0].retrieveInputOutcomes()[0].title == "My Scenario My Suffix"
    }
    def "should support forks and joins in the processing path"() {
        given:
        def filterChainConfig = new FilterChainConfig();

        def inputLinkConfig1 = new InputLinkConfig("input1", DummyInputStrategy.class.getName(), Paths.get("/does/not/exits"))
        filterChainConfig.setInputs(Arrays.asList(inputLinkConfig1))

        def processorLinkConfig1 = new ProcessorLinkConfig("processor1", DummyProcessingStrategy.class.getName(), Arrays.asList("input1"))
        processorLinkConfig1.setProperties(new Properties());
        processorLinkConfig1.getProperties().setProperty("suffix", " 1")
        def processorLinkConfig2 = new ProcessorLinkConfig("processor2", DummyProcessingStrategy.class.getName(), Arrays.asList("input1"))
        processorLinkConfig2.setProperties(new Properties());
        processorLinkConfig2.getProperties().setProperty("suffix", " 2")
        filterChainConfig.setProcessors(Arrays.asList(processorLinkConfig1,processorLinkConfig2))

        def outputLinkConfig = new OutputLinkConfig("output1", DummyOutputStrategy.class.getName(), new File("/does/not/exsit/either"), Arrays.asList("processor1", "processor2"))
        filterChainConfig.setOutputs(Arrays.asList(outputLinkConfig))
        when:

        def outputs = filterChainConfig.buildLinks();

        then:
        outputs.size() == 1
        outputs[0].sources.size() == 2
        ((ProcessorLink)outputs[0].sources[0]).sources.size() == 1
        ((ProcessorLink)outputs[0].sources[1]).sources.size() == 1
        outputs[0].retrieveInputOutcomes()[0].title == "My Scenario 1"
        outputs[0].retrieveInputOutcomes()[1].title == "My Scenario 2"
    }

}
