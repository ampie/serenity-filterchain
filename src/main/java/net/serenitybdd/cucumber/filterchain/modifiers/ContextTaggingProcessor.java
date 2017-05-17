package net.serenitybdd.cucumber.filterchain.modifiers;

import net.serenitybdd.cucumber.adaptor.Contextualizer;
import net.thucydides.core.model.TestOutcome;

public class ContextTaggingProcessor extends AbstractModifier {
    private Contextualizer contextualizer = new Contextualizer();


    @Override
    protected TestOutcome modify(TestOutcome testOutcome) {
        return contextualizer.contextualize(testOutcome);
    }


    public void setSourceContext(String ctx) {
        contextualizer.setContext(ctx);
    }

    public String getSourceContext() {
        return contextualizer.getContext();
    }

    public String getScenarioStatus() {
        return contextualizer.getScenarioStatus();
    }

    public void setScenarioStatus(String scenarioStatus) {
        contextualizer.setScenarioStatus(scenarioStatus);
    }
}
