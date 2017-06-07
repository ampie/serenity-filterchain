package net.serenitybdd.cucumber.filterchain.filters;

import net.serenitybdd.cucumber.filterchain.ProcessingStrategy;
import net.thucydides.core.model.TestOutcome;

public class StoryTypeFilter extends AbstractFilter implements ProcessingStrategy {
    private String[] excludedStoryTypes = new String[0];

    private String[] includedStoryTypes = new String[0];
    private boolean includeNullStories = false;
    private boolean includeNullStoryTypes = false;

    public String[] getExcludedStoryTypes() {
        return excludedStoryTypes;
    }

    public void setExcludedStoryTypes(String[] excludedStoryTypes) {
        this.excludedStoryTypes = excludedStoryTypes;
    }

    public String[] getIncludedStoryTypes() {
        return includedStoryTypes;
    }

    public void setIncludedStoryTypes(String[] includedStoryTypes) {
        this.includedStoryTypes = includedStoryTypes;
    }

    public boolean isIncludeNullStories() {
        return includeNullStories;
    }

    public void setIncludeNullStories(boolean includeNullStories) {
        this.includeNullStories = includeNullStories;
    }

    @Override
    protected boolean shouldRetain(TestOutcome testOutcome) {
        if (testOutcome.getUserStory() == null) {
            return includeNullStories;
        } else if (testOutcome.getUserStory().getType() == null) {
            return includeNullStoryTypes;
        } else if (includedStoryTypes.length > 0) {
            for (String includedStoryType : includedStoryTypes) {
                if (testOutcome.getUserStory().getType().equals(includedStoryType)) {
                    return true;
                }
            }
            return false;
        } else if (excludedStoryTypes.length > 0) {
            for (String excludedStoryType : excludedStoryTypes) {
                if (testOutcome.getUserStory().getType().equals(excludedStoryType)) {
                    return false;
                }
            }
            return true;
        }
        throw new IllegalStateException("Either 'excludedStoryTypes' or 'includedStoryTypes' needs to be set");
    }
}
