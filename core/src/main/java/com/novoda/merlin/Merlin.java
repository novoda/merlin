package com.novoda.merlin;

import com.novoda.merlin.registerable.Registrar;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.service.MerlinServiceBinder;
import com.novoda.merlin.service.ResponseCodeValidator;

public class Merlin {

    private final MerlinServiceBinder merlinServiceBinder;
    private final Registrar registrar;

    Merlin(MerlinServiceBinder merlinServiceBinder, Registrar registrar) {
        this.merlinServiceBinder = merlinServiceBinder;
        this.registrar = registrar;
    }

    public void setEndpoint(Endpoint endpoint, ResponseCodeValidator validator) {
        merlinServiceBinder.setEndpoint(endpoint, validator);
    }

    public void bind() {
        merlinServiceBinder.bindService();
    }

    public void unbind() {
        merlinServiceBinder.unbind();
        registrar.clearRegistrations();
    }

    public void registerConnectable(Connectable connectable) {
        registrar.registerConnectable(connectable);
    }

    public void registerDisconnectable(Disconnectable disconnectable) {
        registrar.registerDisconnectable(disconnectable);
    }

    public void registerBindable(Bindable bindable) {
        registrar.registerBindable(bindable);
    }

    public static class Builder extends MerlinBuilder {
    }

}
