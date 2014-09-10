package com.novoda.merlin.registerable;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinException;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

public class Registerer {

    private final MerlinConnector<Connectable> merlinConnector;
    private final MerlinConnector<Disconnectable> merlinDisconnector;
    private final MerlinConnector<Bindable> merlinOnBinder;

    public Registerer(MerlinConnector<Connectable> merlinConnector, MerlinConnector<Disconnectable> merlinDisconnector, MerlinConnector<Bindable> merlinOnBinder) {
        this.merlinConnector = merlinConnector;
        this.merlinDisconnector = merlinDisconnector;
        this.merlinOnBinder = merlinOnBinder;
    }

    public void registerConnectable(Connectable connectable) {
        getReconnector().register(connectable);
    }

    private MerlinConnector<Connectable> getReconnector() {
        if (merlinConnector == null) {
            throw new MerlinException(
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withConnectableCallbacks()" +
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
                    "You must call " + Merlin.Builder.class.getSimpleName() + ".withDisconnectableCallbacks()" +
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
        MerlinLog.d(object.getClass().getSimpleName() + "does not implement " + Connectable.class + " / " + Disconnectable.class + " / " + Bindable.class);
    }

}