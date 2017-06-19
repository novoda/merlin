package com.novoda.merlin.registerable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class MerlinRegisterer<T extends Registerable> implements MerlinConnector<T> {

    private final List<T> registerableList;

    public MerlinRegisterer() {
        registerableList = new ArrayList<>();
    }

    @Override
    public void register(T what) {
        if (!registerableList.contains(what)) {
            registerableList.add(what);
        }
    }

    @Override
    public List<T> get() {
        return unmodifiableList(registerableList);
    }

}
