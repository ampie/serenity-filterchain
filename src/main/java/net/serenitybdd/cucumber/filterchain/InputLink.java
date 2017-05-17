package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.nio.file.Path;
import java.util.List;

public class InputLink extends TestOutcomeLink<InputStrategy> implements  ProducingLink{
    private Path sourcePath;
    private List<TestOutcome> output;

    public Path getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
    }


    @Override
    public List<TestOutcome> produce() {
        if(this.output==null){
            this.output=getImplementation().extract(sourcePath.toFile());
        }
        return this.output;
    }
}
