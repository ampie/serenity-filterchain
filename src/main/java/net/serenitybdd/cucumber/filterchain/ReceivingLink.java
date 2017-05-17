package net.serenitybdd.cucumber.filterchain;

public interface ReceivingLink {
    void addSource(ProducingLink source);

    String getName();
}
