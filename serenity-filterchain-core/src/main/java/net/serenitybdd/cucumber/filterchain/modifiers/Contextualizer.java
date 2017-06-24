package net.serenitybdd.cucumber.filterchain.modifiers;

import gherkin.formatter.model.Feature;
import net.serenitybdd.cucumber.filterchain.CucumberTagName;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class applies the concept of a {@link net.thucydides.core.ThucydidesSystemProperty#CONTEXT} to the Story of a TestOutcome.
 * It also extracts contextual issue numbers from the tags to make them available as normal issue numbers for downstream
 * operations. This class is ideally used in conjunction with the {@link net.serenitybdd.cucumber.requirement.MultiContextFileSystemTagProvider}
 * class to allow reports and JIRA operations to treat different contexts as separate requirements.
 */
public class Contextualizer {
    private String context;
    private String scenarioStatus;


    protected String contextualizeId(String id) {
        return context == null ? id : id + "-" + context;
    }

    protected String contextualizePath(String path) {
        if (path != null && path.indexOf('.') > 0) {
            String s = path.substring(0, path.lastIndexOf('.')) + "(" + context + ")" + path.substring(path.lastIndexOf('.'));
            return s;
        } else {
            return contextualizeName(path);
        }

    }

    protected String contextualizeName(String name) {
        return context == null ? name : name + " (" + context + ")";
    }

    protected String typeOf(Feature feature) {
        String s = CucumberTagName.REQUIREMENT_TYPE.valueOn(feature);
        return s == null ? "feature" : s;
    }


    public TestOutcome contextualize(TestOutcome sourceOutcome) {
        TestOutcome outcome = sourceOutcome.withMethodName(contextualizeName(sourceOutcome.getName()));
        setContext(outcome);
        outcome.setTitle(contextualizeName(sourceOutcome.getTitle()));
        outcome.setUserStory(contextualizeStory(sourceOutcome.getUserStory()));
        if (getContext() != null) {
            outcome.addTag(TestTag.withValue("Context:" + getContext()));
        }
        if (getScenarioStatus() != null) {
            outcome.addTag(TestTag.withValue("Scenario Status:" + getScenarioStatus()));
        }
        setContextualIssues(outcome);
        return outcome;
    }

    private Story contextualizeStory(Story story) {
        String requirementType = story.getType();
        if (getContext() != null) {
            requirementType = "low level " + requirementType;
        }
        return new Story(
                contextualizeId(story.getId()),
                contextualizeName(story.getName()),
                null,
                contextualizePath(story.getPath()),
                null,
                story.getNarrative(),
                requirementType);
    }

    private void setContextualIssues(TestOutcome outcome) {
        outcome.addIssues(extractIssuesFor(outcome.getTags(), this.getContext()));
    }

    private void setContext(TestOutcome outcome) {
        try {
            //TODO negotiate this with the Serenity guys
            Field context = TestOutcome.class.getDeclaredField("context");
            context.setAccessible(true);
            context.set(outcome,getContext());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> extractIssuesFor(Collection<TestTag> tags, String context) {
        List<String> issues = new ArrayList<>();
        if (context != null) {
            for (TestTag tag : tags) {
                if (tag.getName().startsWith(context) || tag.getName().startsWith("@" + context)) {
                    Matcher matcher = Pattern.compile("((?<!([A-Z]{1,10})-?)[A-Z]+-\\d+)").matcher(tag.getName());
                    if(matcher.find()) {
                        issues.add("#" + matcher.group());
                    }
                }
            }
        }
        return issues;
    }

    public String decontextualizePath(String qualifier, String path) {
        if (path != null && path.indexOf('.') > 0) {
            String s = path.substring(0, path.lastIndexOf('.') - (qualifier.length() + 2)) + path.substring(path.lastIndexOf('.'));
            return s;
        } else {
            return decontextualizeName(qualifier, path);
        }

    }

    public String decontextualizeName(String qualifier, String name) {
        return name.substring(0, name.length() - (qualifier.length() + 3));
    }

    public void setContext(String ctx) {
        this.context = ctx;
    }

    public String getContext() {
        return context;
    }

    public String getScenarioStatus() {
        return scenarioStatus;
    }

    public void setScenarioStatus(String scenarioStatus) {
        this.scenarioStatus = scenarioStatus;
    }
}
