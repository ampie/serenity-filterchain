
package net.serenitybdd.cucumber.filterchain;

import java.io.File;
import java.util.List;

public class OutputLinkConfig extends LinkConfig<OutputLink> {
    private File outputDirectory;
    private List<String> sources;

    public OutputLinkConfig() {
    }

    public OutputLinkConfig(String name, String implementationClass, File outputDirectory, List<String> sources) {
        super(name, implementationClass);
        setOutputDirectory(outputDirectory);
        setSources(sources);
    }

    @Override
    public OutputLink instantiate() {
        OutputLink link = new OutputLink();
        link.setOutputDirectory(getOutputDirectory());
        return link;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }
}
