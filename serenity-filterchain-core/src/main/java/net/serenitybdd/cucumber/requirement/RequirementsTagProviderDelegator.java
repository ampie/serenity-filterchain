package net.serenitybdd.cucumber.requirement;

import com.google.common.base.Optional;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;
import net.thucydides.core.requirements.RequirementsTagProvider;
import net.thucydides.core.requirements.model.Requirement;

import java.util.List;
import java.util.Set;

public class RequirementsTagProviderDelegator implements RequirementsTagProvider {
    private RequirementsTagProvider delegate;

    public void setDelegate(RequirementsTagProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Requirement> getRequirements() {
        return delegate.getRequirements();
    }

    @Override
    public Optional<Requirement> getParentRequirementOf(TestOutcome testOutcome) {
        return delegate.getParentRequirementOf(testOutcome);
    }

    @Override
    public Optional<Requirement> getParentRequirementOf(Requirement requirement) {
        return delegate.getParentRequirementOf(requirement);
    }

    @Override
    public Optional<Requirement> getRequirementFor(TestTag testTag) {
        return delegate.getRequirementFor(testTag);
    }

    @Override
    public Set<TestTag> getTagsFor(TestOutcome testOutcome) {
        return delegate.getTagsFor(testOutcome);
    }

    public RequirementsTagProvider getDelegate() {
        return delegate;
    }
}
