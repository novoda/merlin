package com.novoda.merlin.registerable;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinException;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

public class Registrar {

    private final Register<Connectable> connectables;
    private final Register<Disconnectable> disconnectables;
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
        connectables.clear();
        disconnectables.clear();
        bindables.clear();
    }
}
