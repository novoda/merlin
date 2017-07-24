package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.Register;
import com.novoda.merlin.logger.Logger;

public class DisconnectCallbackManager extends MerlinCallbackManager<Disconnectable> implements Disconnectable {

    public DisconnectCallbackManager(Register<Disconnectable> register) {
        super(register);
    }

    @Override
    public void onDisconnect() {
        Logger.d("onDisconnect");
        for (Disconnectable disconnectable : registerables()) {
            disconnectable.onDisconnect();
        }
    }
}
