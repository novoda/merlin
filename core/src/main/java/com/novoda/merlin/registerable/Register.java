package com.novoda.merlin.registerable;

import java.util.List;

public interface Register<T extends Registerable> {
    void register(T what);

    List<T> get();
}
