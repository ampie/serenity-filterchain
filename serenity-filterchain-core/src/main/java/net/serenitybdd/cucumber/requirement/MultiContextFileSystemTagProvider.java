package net.serenitybdd.cucumber.requirement;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import gherkin.formatter.model.Tag;
import net.serenitybdd.cucumber.filterchain.modifiers.Contextualizer;
import net.thucydides.core.model.LastElement;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;
import net.thucydides.core.requirements.AllRequirements;
import net.thucydides.core.requirements.FileSystemRequirementsTagProvider;
import net.thucydides.core.requirements.RequirementsList;
import net.thucydides.core.requirements.RequirementsTagProvider;
import net.thucydides.core.requirements.model.FeatureType;
import net.thucydides.core.requirements.model.Requirement;
import net.thucydides.core.util.EnvironmentVariables;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static net.thucydides.core.util.NameConverter.humanize;

/**
 * A RequirementsTagProvider that treats a story/feature that occurs in separate context as separate requirements with
 * the original story/feature being the parent requirement. Ideally use in conjunction with {@link Contextualizer}
 */
public class MultiContextFileSystemTagProvider extends FileSystemRequirementsTagProvider {
    private String parentRequirementName;
    private Contextualizer contextualizer = new Contextualizer();
    private HashMap<String, Requirement> requirementMap = new HashMap<>();
    private Map<String, List<TestTag>> requirementTagMap = new HashMap<>();

    public MultiContextFileSystemTagProvider() {
    }

    public MultiContextFileSystemTagProvider(String rootDirectory, int level) {
        super(rootDirectory, level);
    }

    public MultiContextFileSystemTagProvider(String rootDirectory, int level, EnvironmentVariables environmentVariables) {
        super(rootDirectory, level, environmentVariables);
    }

    /**
     * Receives the {@link #requirementMap} and {@link #requirementTagMap} to ensure that all instances of the RequirementTagProvider
     * share the same unique mechanisms for finding requirements and their tags
     */
    public MultiContextFileSystemTagProvider(String path, int i, EnvironmentVariables environmentVariables, Map<String, List<TestTag>> requirementTagMap, HashMap<String, Requirement> requirementMap) {
        this(path, i, environmentVariables);
        this.requirementTagMap = requirementTagMap;
        this.requirementMap = requirementMap;
        this.parentRequirementName = humanize(LastElement.of(path));
    }


    @Override
    public List<Requirement> getRequirements() {
        //This will initialize the entire tree available in the feature/story files, but not the context-specific layer
        List<Requirement> requirements = super.getRequirements();
        if (level == 0 && requirementMap.isEmpty()) {
            //Now add the entire tree to the requirementMap so that subsequent calls to getParentRequirement can use exactly
            //the same instances
            List<Requirement> list = RequirementsList.of(requirements).asFlattenedList();
            for (Requirement requirement : list) {
                requirementMap.put(keyFor(requirement), requirement);
            }
        }
        return requirements;
    }


    /**
     * Overridden because of inconsistency in the use of qualifiedName vs name here in Story.asTag() nad FileSystemTagProvider
     */
    @Override
    public Optional<Requirement> getRequirementFor(TestTag testTag) {
        for (Requirement requirement : AllRequirements.in(getRequirements())) {
            if ((requirement.getName().equalsIgnoreCase(testTag.getName()) || requirement.qualifiedName().equalsIgnoreCase(testTag.getName())) && requirement.getType().equalsIgnoreCase(testTag.getType())) {
                return Optional.of(requirement);
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<Requirement> getParentRequirementOf(TestOutcome testOutcome) {
        Optional<Requirement> result = super.getParentRequirementOf(testOutcome);
        boolean requiresContextualizedRequirement = false;
        if (!result.isPresent()) {
            requiresContextualizedRequirement = testOutcome.getUserStory() != null;//Can't do anything without a userstory
        } else if (testOutcome.getUserStory() != null) {
            //The contextualized userstory would have the context suffixed, but  requirement may not
            requiresContextualizedRequirement = !testOutcome.getUserStory().getName().equals(result.get().getName());
        }
        if (requiresContextualizedRequirement) {
            result = Optional.fromNullable(this.requirementMap.get(requirementKey(testOutcome)));
            if (!result.isPresent() && testOutcome.getContext() != null) {
                Requirement parentRequirement = this.requirementMap.get(parentRequirementKey(testOutcome));
                if (parentRequirement != null) {
                    result = Optional.of(createChildRequirementFor(parentRequirement, testOutcome));
                }
            }
        }
        return result;
    }

    private Requirement createChildRequirementFor(Requirement parentRequirement, TestOutcome testOutcome) {
        String cardNumber = determineCardNumber(testOutcome, parentRequirement);
        //NB!! Use UserStory.getName because if we use UserStory.getId it does not match it up with the tag
        Requirement child = Requirement.named(testOutcome.getUserStory().getName()).
                withOptionalDisplayName(testOutcome.getUserStory().getName()).
                withOptionalCardNumber(cardNumber).
                withType(testOutcome.getUserStory().getType()).
                withNarrative(testOutcome.getUserStory().getNarrative()).
                withParent(parentRequirement.getName()).
                withFeatureFileyName(testOutcome.getUserStory().getPath());
        this.requirementMap.put(keyFor(child), child);
        List<Requirement> children = new ArrayList<>(parentRequirement.getChildren());
        children.add(child);
        parentRequirement.setChildren(children);
        return child;
    }

    private String parentRequirementKey(TestOutcome testOutcome) {
        String path = testOutcome.getUserStory().getPath();
        if (path.lastIndexOf('/') == -1) {
            return "non-existing";
        }
        path = path.substring(0, path.lastIndexOf('/'));
        String grandParentRequirementName = humanize(path.substring(path.lastIndexOf('/') + 1));
        String result = grandParentRequirementName + "/" + decontextualizedStoryName(testOutcome);
        return result.toLowerCase();
    }

    private String requirementKey(TestOutcome testOutcome) {
        return (decontextualizedStoryName(testOutcome) + "/" + testOutcome.getUserStory().getName()).toLowerCase();
    }

    private String keyFor(Requirement child) {
        return ((StringUtils.isNotEmpty(child.getParent())) ? child.getParent() + "/" + child.getName() : child.getName()).toLowerCase();
    }

    @Override
    public Set<TestTag> getTagsFor(TestOutcome testOutcome) {
        Set<TestTag> tags = super.getTagsFor(testOutcome);
//       TODO this is required because of the way we generate the cucumber json file in WireMock - it doesn't take the Feature tags into account.
// Solution1: generate the serenity json files from wiremock.
        //Solution 2: fix the tags inside WireMock
        File file = new File(testOutcome.getUserStory().getPath());
        if (this.requirementTagMap.containsKey(file.getName())) {
            tags.addAll(this.requirementTagMap.get(file.getName()));
        }
        return tags;
    }

    private String determineCardNumber(TestOutcome testOutcome, Requirement parentRequirement) {
        String cardNumber = null;
        List<TestTag> parentTags = this.requirementTagMap.get(parentRequirement.getFeatureFileName());
        if (parentTags != null) {
            List<String> issuesForContext = Contextualizer.extractIssuesFor(parentTags, testOutcome.getContext());
            if (issuesForContext.size() > 0) {
                cardNumber = issuesForContext.get(0);
            }
        }

        return cardNumber;
    }

    /**
     * Overridden to ensure that children of the directory are also processed with MultiSourceContextFileSystemTagProviders
     *
     * @param requirementDirectory
     * @return
     */
    protected List<Requirement> readChildrenFrom(File requirementDirectory) {
        String childDirectory = rootDirectory + "/" + requirementDirectory.getName();
        if (childrenExistFor(childDirectory)) {
            MultiContextFileSystemTagProvider childReader = new MultiContextFileSystemTagProvider(childDirectory, level + 1, environmentVariables, requirementTagMap, requirementMap);
            return childReader.getRequirements();
        } else if (childrenExistFor(requirementDirectory.getPath())) {
            MultiContextFileSystemTagProvider childReader = new MultiContextFileSystemTagProvider(requirementDirectory.getPath(), level + 1, environmentVariables, requirementTagMap, requirementMap);
            return childReader.getRequirements();
        } else {
            return Lists.newArrayList();
        }
    }

    private String decontextualizedStoryName(TestOutcome testOutcome) {
        return contextualizer.decontextualizeName(testOutcome.getContext(), testOutcome.getUserStory().getName());
    }

    /**
     * Overridden to build up a map of FeatureFilename-Tags for later use
     *
     * @param storyFile
     * @return
     */
    public Requirement readRequirementsFromStoryOrFeatureFile(File storyFile) {
        FeatureType type = featureTypeOf(storyFile);
        Requirement requirement = super.readRequirementsFromStoryOrFeatureFile(storyFile);
        List<TestTag> tags = new ArrayList<TestTag>();
        if (type == FeatureType.FEATURE) {
            //TODO consider creating child requirements here. We are already parsing the file
            //Although that would require knowledge of the possible context that are available
            try {
                CucumberTagParser cucumberTagParser = new CucumberTagParser(readLocaleFromFeatureFile(storyFile), environmentVariables);
                cucumberTagParser.parse(storyFile);
                for (Tag tag : cucumberTagParser.getTags()) {
                    tags.add(TestTag.withName(tag.getName()).andType("tag"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO support for jBehave
        }
        requirementTagMap.put(requirement.getFeatureFileName(), tags);
        return requirement;

    }

    private String readLocaleFromFeatureFile(File storyFile) throws IOException {
        List<String> featureFileLines = FileUtils.readLines(storyFile, "UTF-8");
        for (String line : featureFileLines) {
            if (line.startsWith("#") && line.contains("language:")) {
                return line.substring(line.indexOf("language:") + 10).trim();
            }
        }
        return Locale.getDefault().getLanguage();
    }

    private FeatureType featureTypeOf(File storyFile) {
        return (storyFile.getName().endsWith("." + STORY_EXTENSION)) ? FeatureType.STORY : FeatureType.FEATURE;
    }

}
