package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.MerlinRegisterer;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.connection.Connector;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.registerable.disconnection.Disconnector;
import com.novoda.merlin.service.BindListener;
import com.novoda.merlin.service.MerlinServiceBinder;

public class MerlinBuilder {

    private BindListener bindListener;
    private ConnectListener merlinReconnector;
    private DisconnectListener merlinDisconnector;
    private MerlinRegisterer<Connectable> connectableRegisterer;
    private MerlinRegisterer<Disconnectable> disconnectableRegisterer;

    MerlinBuilder() {
    }

    public MerlinBuilder withConnectableCallbacks() {
        connectableRegisterer = new MerlinRegisterer<Connectable>();
        this.merlinReconnector = new Connector(connectableRegisterer);
        return this;
    }

    public MerlinBuilder withDisconnectableCallbacks() {
        disconnectableRegisterer = new MerlinRegisterer<Disconnectable>();
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