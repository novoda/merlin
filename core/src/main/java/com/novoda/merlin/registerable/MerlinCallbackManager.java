package com.novoda.merlin.registerable;

import java.util.List;

public class MerlinCallbackManager<T extends Registerable> {

    private final MerlinConnector<T> merlinConnector;

    public MerlinCallbackManager(MerlinConnector<T> merlinConnector) {
        this.merlinConnector = merlinConnector;
    }

    protected List<T> getRegisterables() {
        return merlinConnector.get();
    }

}
