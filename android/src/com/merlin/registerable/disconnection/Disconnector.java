package com.merlin.registerable.disconnection;

import com.merlin.registerable.MerlinCallbackManager;
import com.merlin.registerable.MerlinConnector;
import com.merlin.Log;

public class Disconnector extends MerlinCallbackManager<Disconnectable> implements DisconnectListener {

    public Disconnector(MerlinConnector<Disconnectable> merlinConnector) {
        super(merlinConnector);
    }

    @Override
    public void onDisconnect() {
        Log.d("onDisconnect");
        for (Disconnectable disconnectable : getMerlinConnectors()) {
            disconnectable.onDisconnect();
        }
    }
}
