package net.serenitybdd.cucumber.filterchain;

import java.io.File;

public class DummyPublishingStrategy implements PublishingStrategy {
    @Override
    public void copy(File source, String destination) {

    }
}
