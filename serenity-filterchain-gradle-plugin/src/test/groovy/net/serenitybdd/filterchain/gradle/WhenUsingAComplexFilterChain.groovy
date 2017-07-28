package net.serenitybdd.filterchain.gradle

import net.serenitybdd.cucumber.filterchain.TagProperty
import net.serenitybdd.cucumber.filterchain.inputs.CucumberImporter
import net.serenitybdd.cucumber.filterchain.integrators.JiraUpdatingProcessor
import net.serenitybdd.cucumber.filterchain.modifiers.ContextTaggingProcessor
import net.serenitybdd.cucumber.filterchain.modifiers.RequirementsTaggingProcessor
import net.serenitybdd.cucumber.filterchain.modifiers.TagRemover
import net.serenitybdd.cucumber.filterchain.outputs.AggregateHtmlOutput
import net.serenitybdd.cucumber.filterchain.outputs.StandardSerenityOutput
import net.serenitybdd.cucumber.filterchain.publishers.WebDavPublishingStrategy
import net.thucydides.core.ThucydidesSystemProperty
import spock.lang.Specification

import java.nio.file.Paths

class WhenUsingAComplexFilterChain  extends Specification{
    def 'doit'(){
        given:
        System.setProperty(ThucydidesSystemProperty.THUCYDIDES_REQUIREMENTS_DIR.propertyName,"/home/ampie/Code/card/card-scenarios/features" )
        System.setProperty(ThucydidesSystemProperty.THUCYDIDES_REQUIREMENT_TYPES.propertyName,"capability,feature,low level feature" )
        def projectDir = "."
        def config = new FilterChainConfig()
        config.input {
            name = 'cucumber-android'
            implementationClass = CucumberImporter.name
            sourcePath = Paths.get("/home/ampie/Code/card/card-output/android.json")
        }
        config.input {
            name = 'cucumber-service'
            implementationClass = CucumberImporter.name
            sourcePath = Paths.get("/home/ampie/Code/card/card-output/service.json")
        }
        config.input {
            name = 'cucumber-cps'
            implementationClass = CucumberImporter.name
            sourcePath = Paths.get("/home/ampie/Code/card/card-output/cps.json")
        }
        config.processor {
            sources = Arrays.asList('cucumber-service')
            name = 'service-contextualizer'
            implementationClass = ContextTaggingProcessor.name
            properties = new Properties()
            properties.setProperty('sourceContext', 'service')
            properties.setProperty('scenarioStatus', 'wip')
        }
        config.processor {
            sources = Arrays.asList('cucumber-cps')
            name = 'cps-contextualizer'
            implementationClass = ContextTaggingProcessor.name
            properties = new Properties()
            properties.setProperty('sourceContext', 'cps')
            properties.setProperty('scenarioStatus', 'wip')
        }
        config.processor {
            sources = Arrays.asList('cucumber-android')
            name = 'android-contextualizer'
            implementationClass = ContextTaggingProcessor.name
            properties = new Properties()
            properties.setProperty('sourceContext', 'android')
            properties.setProperty('scenarioStatus', 'wip')
        }
        config.processor {
            sources = Arrays.asList('android-contextualizer','service-contextualizer','cps-contextualizer')
            name = 'requirements-tagger'
            implementationClass = RequirementsTaggingProcessor.name
        }
        config.processor {
            sources = Arrays.asList('requirements-tagger')
            name = 'tag-remover'
            implementationClass = TagRemover.name
            properties = new Properties()
            properties.setProperty('excludedTags', 'android_,ios_,cps_,service_,dev')
            properties.setProperty('tagProperty', TagProperty.COMPLETE_NAME_CONTAINS.name())
        }
        System.setProperty("serenity.jira.workflow","cli-workflow.groovy");
        System.setProperty("serenity.reports.show.step.details","true");
        System.setProperty("serenity.report.show.manual.tests","false");
        System.setProperty("serenity.issue.tracker.url","http://jira.standardbank.co.za:8091/browse/{0}");
        System.setProperty("jira.root.issue.type","\"Technical Issue\"");
        System.setProperty("jira.root.issue.additional.jql","labels = CSF AND labels = Test");
        System.setProperty("jira.requirement.links","sub-task");
        System.setProperty("jira.url","http://jira.standardbank.co.za:8091");
        System.setProperty("jira.project","PW");
        System.setProperty("jira.username","ampie.barnard");
        System.setProperty("jira.password","ainnikki");
        System.setProperty("serenity.jira.workflow.active","true");
        System.setProperty("serenity.skip.jira.updates","false");
        System.setProperty("serenity.public.url","http://localhost:8084/webdav/");
        config.processor {
            sources = Arrays.asList('tag-remover')
            name = 'jira-updater'

            implementationClass = JiraUpdatingProcessor.name
        }
        config.output {
            name = 'html1'
            implementationClass = AggregateHtmlOutput.name
            outputDirectory = new File("${projectDir}/output/test-output")
            sources = Arrays.asList('jira-updater')
        }
        config.output {
            name = 'html2'
            implementationClass = StandardSerenityOutput.name
            outputDirectory = new File("${projectDir}/output/test-output")
            sources = Arrays.asList('jira-updater')
        }
        config.publisher {
            name = 'webdav'
            sources = Arrays.asList('html2')
            implementationClass = WebDavPublishingStrategy.name
            destination = 'http://localhost:8084/webdav/'
            properties = new Properties()
            properties.setProperty('username', 'test')
            properties.setProperty('password', 'test')
        }
        when:
        config.buildLinks().each {it ->
//            it.clean()
            it.write()
        }
        config.publishingLinks.each {it ->
            it.publish()
        }
        then:
        new File("${projectDir}/output/test-output").exists()
    }
}
