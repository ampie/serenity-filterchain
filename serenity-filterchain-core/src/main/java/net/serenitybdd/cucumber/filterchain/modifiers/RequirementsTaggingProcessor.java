package net.serenitybdd.cucumber.filterchain.modifiers;

import net.thucydides.core.guice.Injectors;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;
import net.thucydides.core.requirements.RequirementsTagProvider;
import net.thucydides.core.statistics.service.TagProvider;
import net.thucydides.core.statistics.service.TagProviderService;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A processor that adds all the requirement based tags extracted from the RequirementsProvider to TestOutcomes
 */
public class RequirementsTaggingProcessor extends AbstractModifier {

    private TagProviderService tagProviderService;

    @Override
    protected TestOutcome modify(TestOutcome testOutcome) {
        loadRequirements(testOutcome);
        return recalculateTags(testOutcome);
    }

    private TestOutcome recalculateTags(TestOutcome testOutcome) {
        SortedSet<TestTag> effectiveTags = new TreeSet<>();//Sorted will eliminate duplicates
        effectiveTags.addAll(testOutcome.getTags());
        TestOutcome result = testOutcome.withTags(null);
        effectiveTags.addAll(result.getTags());//Force reload of tags from tagProviders
        if(result.getFeatureTag().isPresent()) {
            effectiveTags.add(result.getFeatureTag().get());
        }
        result.setTags(effectiveTags);
        result.addIssues(Contextualizer.extractIssuesFor(effectiveTags, testOutcome.getContext()));
        return result;
    }

    private void loadRequirements(TestOutcome testOutcome) {
        List<TagProvider> tagProviders = getTagProviderService().getTagProviders(testOutcome.getTestSource());
        for (TagProvider tagProvider : tagProviders) {
            if(tagProvider instanceof RequirementsTagProvider){
                RequirementsTagProvider rtp = (RequirementsTagProvider) tagProvider;
                rtp.getParentRequirementOf(testOutcome);
            }
        }
    }

    private TagProviderService getTagProviderService() {
        if (tagProviderService == null) {
            tagProviderService = Injectors.getInjector().getInstance(TagProviderService.class);
        }
        return tagProviderService;
    }
}
