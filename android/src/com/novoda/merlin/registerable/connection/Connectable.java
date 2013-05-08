package com.novoda.merlin.registerable.connection;

import com.novoda.merlin.registerable.Registerable;

public interface Connectable extends Registerable {
    void onConnect();
}