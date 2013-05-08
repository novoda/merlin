package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.connection.ConnectableRegisterer;
import com.novoda.merlin.registerable.connection.Connector;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectableRegisterer;
import com.novoda.merlin.registerable.disconnection.Disconnector;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.service.BindListener;
import com.novoda.merlin.service.MerlinServiceBinder;

public class MerlinBuilder {

    private BindListener bindListener;
    private ConnectListener merlinReconnector;
    private DisconnectListener merlinDisconnector;
    private ConnectableRegisterer connectableRegisterer;
    private DisconnectableRegisterer disconnectableRegisterer;

    MerlinBuilder() {
    }

    public MerlinBuilder withConnectableCallbacks() {
        connectableRegisterer = new ConnectableRegisterer();
        this.merlinReconnector = new Connector(connectableRegisterer);
        return this;
    }

    public MerlinBuilder withDisconnectableCallbacks() {
        disconnectableRegisterer = new DisconnectableRegisterer();
        this.merlinDisconnector = new Disconnector(disconnectableRegisterer);
        return this;
    }

    public MerlinBuilder withBindListener(BindListener bindListener) {
        this.bindListener = bindListener;
        return this;
    }

    public MerlinBuilder withLogging(boolean withLogging) {
        Log.LOGGING = withLogging;
        return this;
    }

    public Merlin build(Context context) {
        MerlinServiceBinder merlinServiceBinder = new MerlinServiceBinder(
                context,
                merlinReconnector,
                merlinDisconnector,
                bindListener);

        return new Merlin(merlinServiceBinder, connectableRegisterer, disconnectableRegisterer);
    }

}