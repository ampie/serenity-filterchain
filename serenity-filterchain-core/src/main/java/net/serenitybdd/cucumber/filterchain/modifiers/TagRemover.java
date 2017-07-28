package net.serenitybdd.cucumber.filterchain.modifiers;


import net.serenitybdd.cucumber.filterchain.TagProperty;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;

import java.util.Set;
import java.util.TreeSet;

public class TagRemover extends AbstractModifier {
    private String[] includedTags = new String[0];
    private String[] excludedTags = new String[0];
    private TagProperty tagProperty = TagProperty.COMPLETE_NAME;

    public TagProperty getTagProperty() {
        return tagProperty;
    }

    public void setTagProperty(TagProperty tagProperty) {
        this.tagProperty = tagProperty;
    }

    public String[] getIncludedTags() {
        return includedTags;
    }

    public void setIncludedTags(String[] includedTags) {
        this.includedTags = includedTags;
    }

    public String[] getExcludedTags() {
        return excludedTags;
    }

    public void setExcludedTags(String[] excludedTags) {
        this.excludedTags = excludedTags;
    }

    @Override
    protected TestOutcome modify(TestOutcome testOutcome) {
        Set<TestTag> newTags = new TreeSet<>();
        for (TestTag testTag : testOutcome.getTags()) {
            if (shouldRetain(testTag)) {
                newTags.add(testTag);
            }
        }
        return testOutcome.withTags(newTags);
    }

    private boolean shouldRetain(TestTag tag) {
        if (includedTags.length > 0) {
            for (String includedTag : includedTags) {
                if(!tagProperty.isNull(tag) && tagProperty.isMatch(tag,includedTag)){
                    return true;
                }
            }
            return false;
        }
        if (excludedTags.length > 0) {
            for (String excludedTag : excludedTags) {
                if(!tagProperty.isNull(tag) && tagProperty.isMatch(tag,excludedTag)){
                    return false;
                }
            }
            return true;
        }
        throw new IllegalStateException("Either 'includedTags' or 'excludedTags' should be set");
    }


}
