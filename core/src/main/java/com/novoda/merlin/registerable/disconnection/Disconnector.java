package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.CallbacksRegister;
import com.novoda.support.Logger;

public class Disconnector extends MerlinCallbackManager<Disconnectable> implements DisconnectListener {

    public Disconnector(CallbacksRegister<Disconnectable> callbacksRegister) {
        super(callbacksRegister);
    }

    @Override
    public void onDisconnect() {
        Logger.d("onDisconnect");
        for (Disconnectable disconnectable : getRegisterables()) {
            disconnectable.onDisconnect();
        }
    }
}
