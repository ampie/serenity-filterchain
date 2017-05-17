package net.serenitybdd.cucumber.filterchain;

import java.nio.file.Path;

public class InputLinkConfig extends LinkConfig<InputLink> {
    private Path sourcePath;

    public InputLinkConfig() {
    }

    public InputLinkConfig(String name, String implementationClass, Path sourcePath) {
        super(name, implementationClass);
        setSourcePath(sourcePath);
    }

    @Override
    public InputLink instantiate() {
        InputLink link = new InputLink();
        link.setSourcePath(getSourcePath());
        return link;
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
    }
}
