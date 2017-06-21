package com.novoda.merlin.registerable;

import java.util.ArrayList;
import java.util.List;

public class Register<T extends Registerable> {

    private final List<T> registerables;

    public Register() {
        registerables = new ArrayList<>();
    }

    public void register(T registerable) {
        if (!registerables.contains(registerable)) {
            registerables.add(registerable);
        }
    }

    public List<T> registerables() {
        return registerables;
    }

    public void clear() {
        registerables.clear();
    }
}
