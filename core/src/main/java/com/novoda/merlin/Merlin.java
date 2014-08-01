package com.novoda.merlin;

import com.novoda.merlin.registerable.Registerer;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.service.MerlinServiceBinder;

public class Merlin {

    public static final String DEFAULT_ENDPOINT = "http://www.android.com";

    private final MerlinServiceBinder merlinServiceBinder;
    private final Registerer registerer;

    Merlin(MerlinServiceBinder merlinServiceBinder, Registerer registerer) {
        this.merlinServiceBinder = merlinServiceBinder;
        this.registerer = registerer;
    }

    public void setEndpoint(String endpoint) {
        merlinServiceBinder.setEndpoint(endpoint);
    }

    public void bind() {
        merlinServiceBinder.bindService();
    }

    public void unbind() {
        merlinServiceBinder.unbind();
    }

    public void registerConnectable(Connectable connectable) {
        registerer.registerConnectable(connectable);
    }

    public void registerDisconnectable(Disconnectable disconnectable) {
        registerer.registerDisconnectable(disconnectable);
    }

    public void registerBindable(Bindable bindable) {
        registerer.registerBindable(bindable);
    }

    public static class Builder extends MerlinBuilder {
    }

}
