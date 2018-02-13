package com.novoda.merlin;

import com.novoda.merlin.Registerable;

public interface Disconnectable extends Registerable {
    void onDisconnect();
}
