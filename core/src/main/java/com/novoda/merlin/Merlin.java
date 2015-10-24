package com.novoda.merlin;

import com.novoda.merlin.registerable.Registerer;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.service.MerlinServiceBinder;

import rx.Observable;


public class Merlin {

    public static final String DEFAULT_ENDPOINT = "http://www.android.com";

    private final MerlinServiceBinder merlinServiceBinder;
    private final Registerer registerer;
    private final RxCallbacksManager rxCallbacksManager;

    Merlin(MerlinServiceBinder merlinServiceBinder, Registerer registerer, RxCallbacksManager rxCallbacksManager) {
        this.merlinServiceBinder = merlinServiceBinder;
        this.registerer = registerer;
        this.rxCallbacksManager = rxCallbacksManager;
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

    public Observable<Boolean> getRxConnectionStatusObervable() {
        if (rxCallbacksManager == null) {
            throw new MerlinException(
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withRxJavaCallbacks()" +
                            " before asking for a " + Observable.class.getSimpleName()
            );
        }
        return rxCallbacksManager.getRxConnectionStatusObservable();
    }

    public static class Builder extends MerlinBuilder {
    }

}
