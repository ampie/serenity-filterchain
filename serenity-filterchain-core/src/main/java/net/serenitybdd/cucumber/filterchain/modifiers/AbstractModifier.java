package net.serenitybdd.cucumber.filterchain.modifiers;


import net.serenitybdd.cucumber.filterchain.ProcessingStrategy;
import net.thucydides.core.model.TestOutcome;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModifier implements ProcessingStrategy {
    @Override
    public List<TestOutcome> process(List<TestOutcome> source) {
        List<TestOutcome> result = new ArrayList<>();
        for (TestOutcome testOutcome : source) {
            TestOutcome modified = modify(testOutcome);
            result.add(modified);
            copyRemainingFields(modified, testOutcome);
        }
        return result;
    }

    private void copyRemainingFields(TestOutcome modified, TestOutcome source) {
        String[] fields = {"context"};
        for (String fieldName : fields) {

            try {
                Field field = TestOutcome.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(source);
                if(value!=null) {
                    field.set(modified, value);
                }
            } catch (NoSuchFieldException e) {


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    protected abstract TestOutcome modify(TestOutcome testOutcome);
}
