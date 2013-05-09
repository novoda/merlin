package com.novoda.merlin.registerable;

import java.util.ArrayList;
import java.util.List;

public class MerlinRegisterer<T extends Registerable> implements MerlinConnector<T> {

    private final List<WeakRegisterableReference<T>> registerableList;

    public MerlinRegisterer() {
        registerableList = new ArrayList<WeakRegisterableReference<T>>();
    }

    @Override
    public void register(T what) {
        WeakRegisterableReference<T> registerableReference = new WeakRegisterableReference<T>(what);
        if (!registerableList.contains(registerableReference)) {
            registerableList.add(registerableReference);
        }
    }

    @Override
    public List<T> get() {
        List<T> listOfWhat = new ArrayList<T>(registerableList.size());
        for (WeakRegisterableReference<T> referenceReference : registerableList) {
            T what = referenceReference.get();
            if (what != null) {
                listOfWhat.add(what);
            }
        }
        return listOfWhat;
    }

}
