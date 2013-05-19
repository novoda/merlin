package com.novoda.merlin;

import com.novoda.merlin.registerable.MerlinConnector;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.service.MerlinServiceBinder;

public class Merlin {

    public static final String DEFAULT_HOSTNAME = "http://www.google.com";

    private final MerlinServiceBinder merlinServiceBinder;

    private final MerlinConnector<Connectable> merlinConnector;
    private final MerlinConnector<Disconnectable> merlinDisconnector;
    private final MerlinConnector<Bindable> merlinOnBinder;

    Merlin(MerlinServiceBinder merlinServiceBinder, MerlinConnector<Connectable> merlinConnector, MerlinConnector<Disconnectable> merlinDisconnector, MerlinConnector<Bindable> merlinOnBinder) {
        this.merlinServiceBinder = merlinServiceBinder;
        this.merlinConnector = merlinConnector;
        this.merlinDisconnector = merlinDisconnector;
        this.merlinOnBinder = merlinOnBinder;
    }

    public void setHostname(String hostname) {
        merlinServiceBinder.setHostname(hostname);
    }

    public void bind() {
        merlinServiceBinder.bindService();
    }

    public void unbind() {
        merlinServiceBinder.unbind();
    }

    public void registerConnectable(Connectable connectable) {
        getReconnector().register(connectable);
    }

    private MerlinConnector<Connectable> getReconnector() {
        if (merlinConnector == null) {
            throw new MerlinException(
                    "You must call " + Builder.class.getSimpleName() + ".withConnectableCallbacks()" +
                            "before registering a " + Connectable.class.getSimpleName()
            );
        }
        return merlinConnector;
    }

    public void registerDisconnectable(Disconnectable disconnectable) {
        getDisconnector().register(disconnectable);
    }

    private MerlinConnector<Disconnectable> getDisconnector() {
        if (merlinDisconnector == null) {
            throw new MerlinException(
                    "You must call " + Builder.class.getSimpleName() + ".withDisconnectableCallbacks()" +
                            "before registering a " + Disconnectable.class.getSimpleName()
            );
        }
        return merlinDisconnector;
    }

    public void registerBindable(Bindable bindable) {
        getOnBinder().register(bindable);
    }

    private MerlinConnector<Bindable> getOnBinder() {
        if (merlinOnBinder == null) {
            throw new MerlinException(
                    "You must call " + Builder.class.getSimpleName() + ".withBindableCallbacks()" +
                            "before registering a " + Bindable.class.getSimpleName()
            );
        }
        return merlinOnBinder;
    }

    public static class Builder extends MerlinBuilder {
    }

}
