package net.serenitybdd.cucumber.filterchain;

public abstract class TestOutcomeLink <T> {
    private String name;
    private T implementation;

    public T getImplementation() {
        return implementation;
    }

    public void setImplementation(T implementation) {
        this.implementation = implementation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
