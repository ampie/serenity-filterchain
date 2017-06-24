package net.serenitybdd.cucumber.filterchain.publishers;

import net.serenitybdd.cucumber.filterchain.PublishingStrategy;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FilePublishingStrategy implements PublishingStrategy {
    @Override
    public void copy(File source, String destination) {
        try {
            FileUtils.copyDirectory(source, new File(destination));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
