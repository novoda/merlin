package com.merlin.registerable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MerlinRegisterer<T extends Registerable> implements MerlinConnector<T> {

    private final List<WeakReference<T>> registerableList;

    public MerlinRegisterer() {
        registerableList = new ArrayList<WeakReference<T>>();
    }

    @Override
    public void register(T what) {
        registerableList.add(new WeakReference<T>(what));
    }

    @Override
    public List<T> get() {
        List<T> listOfWhat = new ArrayList<T>(registerableList.size());
        for (WeakReference<T> referenceReference : registerableList) {
            T what = referenceReference.get();
            if (what != null) {
                listOfWhat.add(what);
            }
        }
        return listOfWhat;
    }

}
