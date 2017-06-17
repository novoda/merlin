package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.Register;
import com.novoda.support.Logger;

public class Disconnector extends MerlinCallbackManager<Disconnectable> implements DisconnectListener {

    public Disconnector(Register<Disconnectable> register) {
        super(register);
    }

    @Override
    public void onDisconnect() {
        Logger.d("onDisconnect");
        for (Disconnectable disconnectable : getRegisterables()) {
            disconnectable.onDisconnect();
        }
    }
}
