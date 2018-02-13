package com.novoda.merlin;

import com.novoda.merlin.Registerable;

public interface Connectable extends Registerable {
    void onConnect();
}
