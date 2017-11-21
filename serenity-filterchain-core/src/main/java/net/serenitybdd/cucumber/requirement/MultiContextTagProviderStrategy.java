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

    private static EnvironmentVariables environmentVariables;
    private static ImmutableSet<? extends TagProvider> tagProviders;
    private static RequirementsTagProviderDelegator requirementsTagProviderDelegator = new RequirementsTagProviderDelegator();

    public MultiContextTagProviderStrategy(EnvironmentVariables environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public static void reset() {
       if(environmentVariables!=null) reload();
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
            reload();
            tagProviders = ImmutableSet.of(
                    requirementsTagProviderDelegator,
                    new InjectedTagProvider(environmentVariables),
                    new ContextTagProvider());
        }
        return tagProviders;
    }

    private static void reload() {
        String rootDirectory = ThucydidesSystemProperty.THUCYDIDES_REQUIREMENTS_DIR.from(environmentVariables, "features");
        requirementsTagProviderDelegator.setDelegate(newMultContextProvider(rootDirectory));
    }

    private static MultiContextFileSystemTagProvider newMultContextProvider(String rootDirectory) {
        return new MultiContextFileSystemTagProvider(rootDirectory, 0);
    }

    @Override
    public boolean hasHighPriority() {
        return true;
    }
}
