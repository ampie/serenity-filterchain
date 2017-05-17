package net.serenitybdd.cucumber.filterchain.modifiers;


import net.serenitybdd.cucumber.filterchain.ProcessingStrategy;
import net.thucydides.core.model.TestOutcome;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModifier implements ProcessingStrategy{
    @Override
    public List<TestOutcome> process(List<TestOutcome> source) {
        List<TestOutcome> result =new ArrayList<>();
        for (TestOutcome testOutcome : result) {
            result.add(modify(testOutcome));
        }
        return result;
    }

    protected abstract TestOutcome modify(TestOutcome testOutcome);
}
