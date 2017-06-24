package net.serenitybdd.cucumber.requirement

import net.serenitybdd.cucumber.filterchain.modifiers.Contextualizer
import net.thucydides.core.model.Story
import net.thucydides.core.model.TestOutcome
import spock.lang.Specification

class WhenUsingAMultiContextFileSystemRequirementsProvider  extends Specification{
    def "should dynamically build the requirement of a test outcome emanating from a specific source context"() {
        given:
        def provider = new MultiContextFileSystemTagProvider("src/test/resources/features",0);
        def contextualizer = new Contextualizer();
        contextualizer.setContext("android")
        def testOutcomeEminatingFromAndroid = contextualizer.contextualize(TestOutcome.forTestInStory("Feature 1 completed successfully", Story.called("Feature 1").withPath("capability1/feature1.feature").asFeature()))

        when:

        def parentRequirement = provider.getParentRequirementOf(testOutcomeEminatingFromAndroid).get()

        then:
        parentRequirement.name=="Feature 1 (android)"
        parentRequirement.cardNumber == "#PW-1235"
        parentRequirement.type== "low level feature"
        parentRequirement.featureFileName == "capability1/feature1(android).feature"
    }


}
