package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.registerable.Registerable;

public interface Disconnectable extends Registerable {
    void onDisconnect();
}
