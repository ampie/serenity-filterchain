package net.serenitybdd.cucumber.filterchain.publishers;

import com.sbg.bdd.resource.*;
import com.sbg.bdd.resource.file.DirectoryResourceRoot;
import net.serenitybdd.cucumber.filterchain.PublishingStrategy;
import net.thucydides.core.reports.NumberOfThreads;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractResourcePublishingStrategy implements PublishingStrategy {
    ExecutorService executorPool = Executors.newFixedThreadPool(NumberOfThreads.forIOOperations());
    protected abstract ResourceRoot getDestination(String destination);

    @Override
    public void copy(File source, String destination) {
        DirectoryResourceRoot sourceRoot = new DirectoryResourceRoot("sourceRoot", source);
        ResourceRoot targetRoot = getDestination(destination);
        publishRecursively(sourceRoot, targetRoot);
        try {
            executorPool.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorPool.shutdown();
    }

    private void publishRecursively(ResourceContainer sourceContainer, final ResourceContainer targetContainer) {
        Resource[] list = sourceContainer.list();
        for (final Resource source : list) {
            Resource target = targetContainer.getChild(source.getName());
            if (source instanceof ResourceContainer) {
                if (target == null) {
                    target = targetContainer.resolvePotentialContainer(source.getName());
                }
                publishRecursively((ResourceContainer) source, (ResourceContainer) target);
            } else if (source instanceof ReadableResource) {
                executorPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Resource target = targetContainer.resolvePotential(source.getName());
                        ((WritableResource) target).write(((ReadableResource) source).read());
                    }
                });
            }
        }
    }
}