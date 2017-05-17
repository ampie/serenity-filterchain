package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestOutcome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ampie on 2017/05/14.
 */
public class DummyProcessingStrategy implements ProcessingStrategy {
    private String suffix = " Processed";
    @Override
    public List<TestOutcome> process(List<TestOutcome> source) {
        List<TestOutcome> result = new ArrayList<>();
        for (TestOutcome testOutcome : source) {
            TestOutcome output = testOutcome.withId("id" + getSuffix());
            result.add(output);
            output.setTitle(testOutcome.getTitle() + getSuffix());
        }
        return result;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
