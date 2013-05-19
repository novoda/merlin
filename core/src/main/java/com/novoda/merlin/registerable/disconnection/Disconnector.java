package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.Log;
import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.MerlinConnector;

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
