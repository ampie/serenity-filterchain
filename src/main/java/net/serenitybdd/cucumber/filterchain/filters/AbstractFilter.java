package net.serenitybdd.cucumber.filterchain.filters;

import net.serenitybdd.cucumber.filterchain.ProcessingStrategy;
import net.thucydides.core.model.TestOutcome;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilter implements ProcessingStrategy {
    @Override
    public List<TestOutcome> process(List<TestOutcome> source) {
        List<TestOutcome> result = new ArrayList<>();
        for (TestOutcome testOutcome : result) {
            if (shouldRetain(testOutcome)) {
                result.add(testOutcome);
            }
        }
        return result;
    }

    protected abstract boolean shouldRetain(TestOutcome testOutcome);

}
