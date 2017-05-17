package net.serenitybdd.cucumber.filterchain.filters;

import net.serenitybdd.cucumber.filterchain.TagProperty;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;

public class TagBasedFilter extends AbstractFilter {
    private String[] includedTags=new String[0];
    private String[] excludedTags=new String[0];
    private TagProperty tagProperty = TagProperty.COMPLETE_NAME;

    @Override
    protected boolean shouldRetain(TestOutcome testOutcome) {
        if (includedTags.length > 0) {
            for (String includedTag : includedTags) {
                for (TestTag testTag : testOutcome.getTags()) {
                    if (!tagProperty.isNull(testTag) && tagProperty.isMatch(testTag, includedTag)) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (excludedTags.length > 0) {
            for (String excludedTag : excludedTags) {
                for (TestTag testTag : testOutcome.getTags()) {
                    if (!tagProperty.isNull(testTag) && tagProperty.isMatch(testTag, excludedTag)) {
                        return false;
                    }
                }
            }
            return true;
        }
        throw new IllegalStateException("Either 'includedTags' or 'excludedTags' needs to be set");
    }
}
