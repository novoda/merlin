package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.MerlinConnector;
import com.novoda.merlin.Log;

public class Disconnector extends MerlinCallbackManager<Disconnectable> implements DisconnectListener {

    public Disconnector(MerlinConnector<Disconnectable> merlinConnector) {
        super(merlinConnector);
    }

    @Override
    public void onDisconnect() {
        Log.d("onDisconnect");
        for (Disconnectable disconnectable : getRegisterables()) {
            disconnectable.onDisconnect();
        }
    }
}
