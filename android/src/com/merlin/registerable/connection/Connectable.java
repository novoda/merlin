package com.merlin.registerable.connection;

import com.merlin.registerable.Registerable;

public interface Connectable extends Registerable {
    void onConnect();
}