package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.MerlinRegisterer;
import com.novoda.merlin.registerable.Registerer;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.bind.OnBinder;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.connection.Connector;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.registerable.disconnection.Disconnector;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.service.MerlinServiceBinder;

public class MerlinBuilder {

    private BindListener merlinOnBinder;
    private ConnectListener merlinConnector;
    private DisconnectListener merlinDisconnector;

    private MerlinRegisterer<Connectable> connectableRegisterer;
    private MerlinRegisterer<Disconnectable> disconnectableRegisterer;
    private MerlinRegisterer<Bindable> bindableRegisterer;

    MerlinBuilder() {
    }

    public MerlinBuilder withConnectableCallbacks() {
        connectableRegisterer = new MerlinRegisterer<Connectable>();
        this.merlinConnector = new Connector(connectableRegisterer);
        return this;
    }

    public MerlinBuilder withDisconnectableCallbacks() {
        disconnectableRegisterer = new MerlinRegisterer<Disconnectable>();
        this.merlinDisconnector = new Disconnector(disconnectableRegisterer);
        return this;
    }

    public MerlinBuilder withBindableCallbacks() {
        bindableRegisterer = new MerlinRegisterer<Bindable>();
        this.merlinOnBinder = new OnBinder(bindableRegisterer);
        return this;
    }

    public MerlinBuilder withLogging(boolean withLogging) {
        Log.LOGGING = withLogging;
        return this;
    }

    public Merlin build(Context context) {
        MerlinServiceBinder merlinServiceBinder = new MerlinServiceBinder(context, merlinConnector, merlinDisconnector, merlinOnBinder);
        Registerer merlinRegisterer = new Registerer(connectableRegisterer, disconnectableRegisterer, bindableRegisterer);
        return new Merlin(merlinServiceBinder, merlinRegisterer);
    }

}