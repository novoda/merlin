package com.merlin.registerable.disconnection;

import com.merlin.registerable.Registerable;

public interface Disconnectable extends Registerable {
    void onDisconnect();
}
