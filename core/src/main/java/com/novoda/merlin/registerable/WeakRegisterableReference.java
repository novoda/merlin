package com.novoda.merlin.registerable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

class WeakRegisterableReference<T extends Registerable> extends WeakReference<T> {

    public WeakRegisterableReference(T referent) {
        super(referent);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Reference) {
            return get().equals(((Reference) obj).get());
        } else {
            return get().equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }

}