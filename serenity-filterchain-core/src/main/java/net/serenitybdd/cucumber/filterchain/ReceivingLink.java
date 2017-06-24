package net.serenitybdd.cucumber.filterchain;

public interface ReceivingLink {
    void addSource(TestOutcomeLink source);

    String getName();
}
