package net.serenitybdd.filterchain.gradle

import net.serenitybdd.cucumber.filterchain.InputLinkConfig
import net.serenitybdd.cucumber.filterchain.OutputLinkConfig
import net.serenitybdd.cucumber.filterchain.ProcessorLinkConfig
import net.serenitybdd.cucumber.filterchain.PublishingLinkConfig
import org.gradle.api.Project

class FilterChainConfig extends net.serenitybdd.cucumber.filterchain.FilterChainConfig {
    Project project

    FilterChainConfig(Project project) {
        this.project = project
    }

    InputLinkConfig input(Closure closure) {
        InputLinkConfig config = new InputLinkConfig()
        if (inputs == null) {
            inputs = new ArrayList<>();
        }
        inputs.add(config)
        project.configure(config, closure)
        return config;
    }

    ProcessorLinkConfig processor(Closure closure) {
        ProcessorLinkConfig config = new ProcessorLinkConfig()
        if (processors == null) {
            processors = new ArrayList<>();
        }
        processors.add(config)
        project.configure(config, closure)
        return config;
    }
    OutputLinkConfig output(Closure closure) {
        OutputLinkConfig config = new OutputLinkConfig()
        if (outputs == null) {
            outputs = new ArrayList<>();
        }
        outputs.add(config)
        project.configure(config, closure)
        return config;
    }
    PublishingLinkConfig publisher(Closure closure) {
        PublishingLinkConfig config = new PublishingLinkConfig()
        if (publishers == null) {
            publishers = new ArrayList<>();
        }
        publishers.add(config)
        project.configure(config, closure)
        return config;
    }

}