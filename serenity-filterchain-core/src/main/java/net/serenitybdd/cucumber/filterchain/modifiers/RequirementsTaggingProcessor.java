package net.serenitybdd.cucumber.filterchain.modifiers;

import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A processor that adds all the requirement based tags extracted from the RequirementsProvider to TestOutcomes
 */
public class RequirementsTaggingProcessor extends AbstractModifier {

    @Override
    protected TestOutcome modify(TestOutcome testOutcome) {
        SortedSet<TestTag> effectiveTags = new TreeSet<>();//Sorted will eliminate duplicates
        effectiveTags.addAll(testOutcome.getTags());
        TestOutcome result = testOutcome.withTags(null);
        effectiveTags.addAll(result.getTags());//Force reload of tags from tagProviders
        result.setTags(effectiveTags);
        return result;
    }

}
