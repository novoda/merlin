package com.novoda.merlin.registerable;

import java.util.ArrayList;
import java.util.List;

public class MerlinRegisterer<T extends Registerable> implements CallbacksRegister<T> {

    private final List<T> registerables;

    public MerlinRegisterer() {
        registerables = new ArrayList<>();
    }

    @Override
    public void register(T registerable) {
        if (!registerables.contains(registerable)) {
            registerables.add(registerable);
        }
    }

    @Override
    public List<T> get() {
        return registerables;
    }

}
