package com.novoda.merlin.registerable;

import com.novoda.merlin.MerlinException;
import com.novoda.merlin.MerlinRobolectricTestRunner;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MerlinRobolectricTestRunner.class)
public class RegistererShould {

    Registerer registerer;

    @Test(expected = MerlinException.class)
    public void throwADeveloperExceptionWhenAConnectorIsNotSetButRegisterConnectableIsCalled() throws Exception {
        registerer = new Registerer(null, null, null);
        Connectable connectable = mock(Connectable.class);

        registerer.registerConnectable(connectable);
    }

    @Test(expected = MerlinException.class)
    public void throwADeveloperExceptionWhenADisconnectorIsNotSetButRegisterDisconnectableIsCalled() throws Exception {
        registerer = new Registerer(null, null, null);
        Disconnectable disconnectable = mock(Disconnectable.class);

        registerer.registerDisconnectable(disconnectable);
    }

    @Test(expected = MerlinException.class)
    public void throwADeveloperExceptionWhenAOnBinderIsNotSetButRegisterBindableIsCalled() throws Exception {
        registerer = new Registerer(null, null, null);
        Bindable bindable = mock(Bindable.class);

        registerer.registerBindable(bindable);
    }

    @Test
    public void registerObjectsWhichImplementConnectable() throws Exception {
        MerlinConnector<Connectable> connector = mock(MerlinConnector.class);
        Registerer registerer = new Registerer(connector, null, null);
        ConnectableClassImpl reconnectableClass = new ConnectableClassImpl();

        registerer.registerConnectable(reconnectableClass);

        verify(connector).register(reconnectableClass);
    }

    @Test
    public void registerObjectsWhichImplementDisconnectable() throws Exception {
        MerlinConnector<Disconnectable> disconnector = mock(MerlinConnector.class);
        Registerer registerer = new Registerer(null, disconnector, null);
        DisconnectableImpl disconnectableImpl = new DisconnectableImpl();

        registerer.registerDisconnectable(disconnectableImpl);

        verify(disconnector).register(disconnectableImpl);
    }

    @Test
    public void registerObjectsWhichImplementBindable() throws Exception {
        MerlinConnector<Bindable> onBinder = mock(MerlinConnector.class);
        Registerer registerer = new Registerer(null, null, onBinder);
        BindableImpl disconnectableImpl = new BindableImpl();

        registerer.registerBindable(disconnectableImpl);

        verify(onBinder).register(disconnectableImpl);
    }

    @Test
    public void notRegisterObjectsWhichDoNotImplementReconnectable() throws Exception {
        NonRegisterableClass nonReconnectableClass = new NonRegisterableClass();
        MerlinConnector<Connectable> merlinConnector = mock(MerlinConnector.class);
        MerlinConnector<Disconnectable> merlinDisconnector = mock(MerlinConnector.class);
        MerlinConnector<Bindable> merlinOnBinder = mock(MerlinConnector.class);
        Registerer registerer = new Registerer(merlinConnector, merlinDisconnector, merlinOnBinder);

        registerer.register(nonReconnectableClass);

        verify(merlinConnector, never()).register(any(Connectable.class));
        verify(merlinDisconnector, never()).register(any(Disconnectable.class));
        verify(merlinOnBinder, never()).register(any(Bindable.class));
    }

    private static class ConnectableClassImpl implements Connectable {
        @Override
        public void onConnect() {
        }
    }

    private static class DisconnectableImpl implements Disconnectable {
        @Override
        public void onDisconnect() {
        }
    }

    private static class BindableImpl implements Bindable {
        @Override
        public void onBind(NetworkStatus networkStatus) {
        }
    }

    private static class NonRegisterableClass {
    }

}
