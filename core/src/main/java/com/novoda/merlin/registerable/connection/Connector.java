package com.novoda.merlin.registerable.connection;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.MerlinConnector;

public class Connector extends MerlinCallbackManager<Connectable> implements ConnectListener {

    public Connector(MerlinConnector<Connectable> merlinConnector) {
        super(merlinConnector);
    }

    @Override
    public void onConnect() {
        MerlinLog.d("onConnect");
        for (Connectable connectable : getRegisterables()) {
            connectable.onConnect();
        }
    }

}
