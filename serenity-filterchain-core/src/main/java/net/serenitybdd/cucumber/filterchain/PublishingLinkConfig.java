package net.serenitybdd.cucumber.filterchain;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PublishingLinkConfig extends LinkConfig<PublishingLink> {
    private String destination;
    private List<String> sources;
    private List<String> sourceDirectories;

    public PublishingLinkConfig() {
    }

    public PublishingLinkConfig(String name, String implementationClass, String destination, List<String> sources) {
        super(name, implementationClass);
        this.destination = destination;
        this.sources = sources;
    }

    @Override
    public PublishingLink instantiate() {
        PublishingLink link = new PublishingLink();
        link.setDestination(getDestination());
        List<File> sourceDirectoryFiles = new ArrayList<>();
        for (String sourceDirectory : getSourceDirectories()) {
            sourceDirectoryFiles.add(new File(sourceDirectory));
        }
        link.setSourceDirectories(sourceDirectoryFiles);
        return link;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getSources() {
        return sources==null? Collections.EMPTY_LIST:sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getSourceDirectories() {
        return sourceDirectories==null? Collections.EMPTY_LIST:sourceDirectories;
    }

    public void setSourceDirectories(List<String> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }
}
