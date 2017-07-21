package com.novoda.merlin.registerable.connection;

import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.Register;
import com.novoda.merlin.logger.Logger;

public class ConnectCallbackManager extends MerlinCallbackManager<Connectable> {

    public ConnectCallbackManager(Register<Connectable> register) {
        super(register);
    }

    public void onConnect() {
        Logger.d("onConnect");
        for (Connectable connectable : registerables()) {
            connectable.onConnect();
        }
    }

}
