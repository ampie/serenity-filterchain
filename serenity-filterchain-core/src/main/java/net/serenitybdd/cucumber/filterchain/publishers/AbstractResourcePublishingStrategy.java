package net.serenitybdd.cucumber.filterchain.publishers;

import com.sbg.bdd.resource.*;
import com.sbg.bdd.resource.file.DirectoryResourceRoot;
import net.serenitybdd.cucumber.filterchain.PublishingStrategy;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public abstract class AbstractResourcePublishingStrategy implements PublishingStrategy {
    protected abstract ResourceRoot getDestination(String destination);

    @Override
    public void copy(File source, String destination) {
        DirectoryResourceRoot sourceRoot = new DirectoryResourceRoot("sourceRoot", source);
        ResourceRoot targetRoot = getDestination(destination);
        publishRecursively(sourceRoot, targetRoot);
    }

    private void publishRecursively(ResourceContainer sourceContainer, ResourceContainer targetContainer) {
        Resource[] list = sourceContainer.list();
        for (Resource source : list) {
            Resource target = targetContainer.getChild(source.getName());
            if (source instanceof ResourceContainer) {
                if (target == null) {
                    target = targetContainer.resolvePotentialContainer(source.getName());
                }
                publishRecursively((ResourceContainer) source, (ResourceContainer) target);
            } else if (source instanceof ReadableResource) {
                target = targetContainer.resolvePotential(source.getName());
                ((WritableResource) target).write(((ReadableResource) source).read());
            }
        }
    }
}