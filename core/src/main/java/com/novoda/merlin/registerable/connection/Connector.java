package com.novoda.merlin.registerable.connection;

import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.Register;
import com.novoda.support.Logger;

public class Connector extends MerlinCallbackManager<Connectable> implements ConnectListener {

    public Connector(Register<Connectable> register) {
        super(register);
    }

    @Override
    public void onConnect() {
        Logger.d("onConnect");
        for (Connectable connectable : getRegisterables()) {
            connectable.onConnect();
        }
    }

}
