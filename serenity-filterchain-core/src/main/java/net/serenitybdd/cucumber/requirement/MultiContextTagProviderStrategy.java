package net.serenitybdd.cucumber.requirement;

import com.google.common.collect.ImmutableSet;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.requirements.FileSystemRequirementsTagProvider;
import net.thucydides.core.statistics.service.ContextTagProvider;
import net.thucydides.core.statistics.service.InjectedTagProvider;
import net.thucydides.core.statistics.service.TagProvider;
import net.thucydides.core.statistics.service.TagProviderStrategy;
import net.thucydides.core.util.EnvironmentVariables;

public class MultiContextTagProviderStrategy implements TagProviderStrategy {

    private final EnvironmentVariables environmentVariables;
    private static ImmutableSet<? extends TagProvider> tagProviders;

    public MultiContextTagProviderStrategy(EnvironmentVariables environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public MultiContextTagProviderStrategy() {
        this(Injectors.getInjector().getInstance(EnvironmentVariables.class));
    }

    @Override
    public boolean canHandleTestSource(String testSource) {
        return true;
    }

    @Override
    public Iterable<? extends TagProvider> getTagProviders() {
        return create();
    }

    private ImmutableSet<? extends TagProvider> create() {
        if (tagProviders == null) {
            String rootDirectory = ThucydidesSystemProperty.THUCYDIDES_REQUIREMENTS_DIR.from(environmentVariables, "features");
            tagProviders = ImmutableSet.of(
                    new MultiContextFileSystemTagProvider(rootDirectory, 0),
                    new InjectedTagProvider(environmentVariables),
                    new ContextTagProvider());
        }
        return tagProviders;
    }

    @Override
    public boolean hasHighPriority() {
        return true;
    }
}
