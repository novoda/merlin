package com.novoda.merlin.registerable;

import java.util.List;

public class MerlinCallbackManager<T extends Registerable> {

    private final Register<T> register;

    public MerlinCallbackManager(Register<T> register) {
        this.register = register;
    }

    protected List<T> registerables() {
        return register.registerables();
    }

}
