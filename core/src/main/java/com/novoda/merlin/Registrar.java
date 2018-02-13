package com.novoda.merlin;

import android.support.annotation.Nullable;

public class Registrar {

    @Nullable
    private final Register<Connectable> connectables;
    @Nullable
    private final Register<Disconnectable> disconnectables;
    @Nullable
    private final Register<Bindable> bindables;

    public Registrar(Register<Connectable> connectables, Register<Disconnectable> disconnectables, Register<Bindable> bindables) {
        this.connectables = connectables;
        this.disconnectables = disconnectables;
        this.bindables = bindables;
    }

    public void registerConnectable(Connectable connectable) {
        connectables().register(connectable);
    }

    private Register<Connectable> connectables() {
        if (connectables == null) {
            throw new MerlinException(
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withConnectableCallbacks()" +
                            "before registering a " + Connectable.class.getSimpleName()
            );
        }
        return connectables;
    }

    public void registerDisconnectable(Disconnectable disconnectable) {
        disconnectables().register(disconnectable);
    }

    private Register<Disconnectable> disconnectables() {
        if (disconnectables == null) {
            throw new MerlinException(
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withDisconnectableCallbacks()" +
                            "before registering a " + Disconnectable.class.getSimpleName()
            );
        }
        return disconnectables;
    }

    public void registerBindable(Bindable bindable) {
        bindables().register(bindable);
    }

    private Register<Bindable> bindables() {
        if (bindables == null) {
            throw new MerlinException(
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withBindableCallbacks()" +
                            "before registering a " + Bindable.class.getSimpleName()
            );
        }
        return bindables;
    }

    public void clearRegistrations() {
        if (connectables != null) {
            connectables.clear();
        }

        if (disconnectables != null) {
            disconnectables.clear();
        }

        if (bindables != null) {
            bindables.clear();
        }
    }
}
