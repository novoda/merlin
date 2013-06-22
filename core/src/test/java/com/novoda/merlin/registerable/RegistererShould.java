package com.novoda.merlin.registerable;

import com.novoda.merlin.MerlinException;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.robolectric.NovodaRobolectricTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(NovodaRobolectricTestRunner.class)
public class RegistererShould {

    Registerer registerer;

    @Test(expected = MerlinException.class)
    public void throw_a_developer_exception_when_a_Connector_is_not_set_but_registerConnectable_is_called() throws Exception {
        registerer = new Registerer(null, null, null);
        Connectable connectable = mock(Connectable.class);

        registerer.registerConnectable(connectable);
    }

    @Test(expected = MerlinException.class)
    public void throw_a_developer_exception_when_a_Disconnector_is_not_set_but_registerDisconnectable_is_called() throws Exception {
        registerer = new Registerer(null, null, null);
        Disconnectable disconnectable = mock(Disconnectable.class);

        registerer.registerDisconnectable(disconnectable);
    }

    @Test(expected = MerlinException.class)
    public void throw_a_developer_exception_when_a_OnBinder_is_not_set_but_registerBindable_is_called() throws Exception {
        registerer = new Registerer(null, null, null);
        Bindable bindable = mock(Bindable.class);

        registerer.registerBindable(bindable);
    }

    @Test
    public void register_objects_which_implement_Connectable() throws Exception {
        MerlinConnector<Connectable> connector = mock(MerlinConnector.class);
        Registerer registerer = new Registerer(connector, null, null);
        ConnectableClassImpl reconnectableClass = new ConnectableClassImpl();

        registerer.registerConnectable(reconnectableClass);

        verify(connector).register(reconnectableClass);
    }

    @Test
    public void register_objects_which_implement_Disconnectable() throws Exception {
        MerlinConnector<Disconnectable> disconnector = mock(MerlinConnector.class);
        Registerer registerer = new Registerer(null, disconnector, null);
        DisconnectableImpl disconnectableImpl = new DisconnectableImpl();

        registerer.registerDisconnectable(disconnectableImpl);

        verify(disconnector).register(disconnectableImpl);
    }

    @Test
    public void register_objects_which_implement_Bindable() throws Exception {
        MerlinConnector<Bindable> onBinder = mock(MerlinConnector.class);
        Registerer registerer = new Registerer(null, null, onBinder);
        BindableImpl disconnectableImpl = new BindableImpl();

        registerer.registerBindable(disconnectableImpl);

        verify(onBinder).register(disconnectableImpl);
    }

    @Test
    public void not_register_objects_which_do_not_implement_reconnectable() throws Exception {
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
