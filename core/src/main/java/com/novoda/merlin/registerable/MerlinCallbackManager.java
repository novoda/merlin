package com.novoda.merlin.registerable;

import java.util.List;

public class MerlinCallbackManager<T extends Registerable> {

    private final CallbacksRegister<T> callbacksRegister;

    public MerlinCallbackManager(CallbacksRegister<T> callbacksRegister) {
        this.callbacksRegister = callbacksRegister;
    }

    protected List<T> getRegisterables() {
        return callbacksRegister.get();
    }

}
