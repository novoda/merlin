package com.novoda.merlin;

import java.util.ArrayList;
import java.util.List;

class Register<T extends Registerable> {

    private final List<T> registerables;

    Register() {
        registerables = new ArrayList<>();
    }

    void register(T registerable) {
        if (!registerables.contains(registerable)) {
            registerables.add(registerable);
        }
    }

    List<T> registerables() {
        return new ArrayList<>(registerables);
    }

    void clear() {
        registerables.clear();
    }
}
