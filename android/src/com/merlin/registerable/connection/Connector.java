package com.merlin.registerable.connection;

import com.merlin.registerable.MerlinCallbackManager;
import com.merlin.registerable.MerlinConnector;
import com.merlin.Log;

public class Connector extends MerlinCallbackManager<Connectable> implements ConnectListener {

    public Connector(MerlinConnector<Connectable> merlinConnector) {
        super(merlinConnector);
    }

    @Override
    public void onConnect() {
        Log.d("onConnect");
        for (Connectable connectable : getMerlinConnectors()) {
            connectable.onConnect();
        }
    }

}
