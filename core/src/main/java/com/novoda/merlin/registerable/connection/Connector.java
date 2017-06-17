package com.novoda.merlin.registerable.connection;

import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.CallbacksRegister;
import com.novoda.support.Logger;

public class Connector extends MerlinCallbackManager<Connectable> implements ConnectListener {

    public Connector(CallbacksRegister<Connectable> callbacksRegister) {
        super(callbacksRegister);
    }

    @Override
    public void onConnect() {
        Logger.d("onConnect");
        for (Connectable connectable : getRegisterables()) {
            connectable.onConnect();
        }
    }

}
