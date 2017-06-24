package net.serenitybdd.cucumber.filterchain;

import java.io.File;

public interface PublishingStrategy {
    void copy(File source,String destination);
}
