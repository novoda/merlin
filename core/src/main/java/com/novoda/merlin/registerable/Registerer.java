package com.novoda.merlin.registerable;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinException;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.support.Logger;

public class Registerer {

    private final Register<Connectable> register;
    private final Register<Disconnectable> merlinDisconnector;
    private final Register<Bindable> merlinOnBinder;

    public Registerer(Register<Connectable> register, Register<Disconnectable> merlinDisconnector, Register<Bindable> merlinOnBinder) {
        this.register = register;
        this.merlinDisconnector = merlinDisconnector;
        this.merlinOnBinder = merlinOnBinder;
    }

    public void registerConnectable(Connectable connectable) {
        getReconnector().register(connectable);
    }

    private Register<Connectable> getReconnector() {
        if (register == null) {
            throw new MerlinException(
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withConnectableCallbacks()" +
                            "before registering a " + Connectable.class.getSimpleName()
            );
        }
        return register;
    }

    public void registerDisconnectable(Disconnectable disconnectable) {
        getDisconnector().register(disconnectable);
    }

    private Register<Disconnectable> getDisconnector() {
        if (merlinDisconnector == null) {
            throw new MerlinException(
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withDisconnectableCallbacks()" +
                            "before registering a " + Disconnectable.class.getSimpleName()
            );
        }
        return merlinDisconnector;
    }

    public void registerBindable(Bindable bindable) {
        getOnBinder().register(bindable);
    }

    private Register<Bindable> getOnBinder() {
        if (merlinOnBinder == null) {
            throw new MerlinException(
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withBindableCallbacks()" +
                            "before registering a " + Bindable.class.getSimpleName()
            );
        }
        return merlinOnBinder;
    }

    public void register(Object object) {
        if (object instanceof Registerable) {
            Registerable registerable = (Registerable) object;
            if (registerable instanceof Connectable) {
                registerConnectable((Connectable) registerable);
                return;
            }
            if (registerable instanceof Disconnectable) {
                registerDisconnectable((Disconnectable) registerable);
                return;
            }
            if (registerable instanceof Bindable) {
                registerBindable((Bindable) registerable);
                return;
            }
        }
        Logger.d(object.getClass().getSimpleName() + "does not implement " + Connectable.class + " / " + Disconnectable.class + " / " + Bindable.class);
    }

}
