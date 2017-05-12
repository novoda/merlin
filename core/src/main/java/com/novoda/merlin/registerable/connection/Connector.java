package com.novoda.merlin.registerable.connection;

import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.MerlinConnector;
import com.novoda.support.Logger;

public class Connector extends MerlinCallbackManager<Connectable> implements ConnectListener {

    public Connector(MerlinConnector<Connectable> merlinConnector) {
        super(merlinConnector);
    }

    @Override
    public void onConnect() {
        Logger.d("onConnect");
        for (Connectable connectable : getRegisterables()) {
            connectable.onConnect();
        }
    }

}
