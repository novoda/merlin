package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.MerlinConnector;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.service.MerlinServiceBinder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import robolectric.NovodaRobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(NovodaRobolectricTestRunner.class)
public class MerlinShould {

    @Mock private Context context;
    @Mock private MerlinServiceBinder serviceBinder;

    private Merlin merlin;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        merlin = new Merlin(serviceBinder, null, null, null);
    }

    @Test
    public void start_the_merlin_service() throws Exception {
        merlin.bind();

        verify(serviceBinder).bindService();
    }

    @Test
    public void bind_the_merlin_service_with_a_hostname_when_provided() throws Exception {
        String hostname = "start_the_merlin_service_with_a_hostname_when_provided";

        merlin.setHostname(hostname);
        merlin.bind();

        verify(serviceBinder).setHostname(hostname);
    }

    @Test
    public void unbind_the_merlin_service() throws Exception {
        merlin.unbind();

        verify(serviceBinder).unbind();
    }

    @Test(expected = MerlinException.class)
    public void throw_a_developer_exception_when_a_Connector_is_not_set_but_registerConnectable_is_called() throws Exception {
        merlin = new Merlin(serviceBinder, null, null, null);
        Connectable connectable = mock(Connectable.class);

        merlin.registerConnectable(connectable);
    }

    @Test(expected = MerlinException.class)
    public void throw_a_developer_exception_when_a_Disconnector_is_not_set_but_registerDisconnectable_is_called() throws Exception {
        merlin = new Merlin(serviceBinder, null, null, null);
        Disconnectable disconnectable = mock(Disconnectable.class);

        merlin.registerDisconnectable(disconnectable);
    }

    @Test(expected = MerlinException.class)
    public void throw_a_developer_exception_when_a_OnBinder_is_not_set_but_registerBindable_is_called() throws Exception {
        merlin = new Merlin(serviceBinder, null, null, null);
        Bindable bindable = mock(Bindable.class);

        merlin.registerBindable(bindable);
    }

    @Test
    public void register_objects_which_implement_Connectable() throws Exception {
        MerlinConnector<Connectable> connector = mock(MerlinConnector.class);
        Merlin merlin = new Merlin(null, connector, null, null);
        ConnectableClassImpl reconnectableClass = new ConnectableClassImpl();

        merlin.registerConnectable(reconnectableClass);

        verify(connector).register(reconnectableClass);
    }

    @Test
    public void register_objects_which_implement_Disconnectable() throws Exception {
        MerlinConnector<Disconnectable> disconnector = mock(MerlinConnector.class);
        Merlin merlin = new Merlin(null, null, disconnector, null);
        DisconnectableImpl disconnectableImpl = new DisconnectableImpl();

        merlin.registerDisconnectable(disconnectableImpl);

        verify(disconnector).register(disconnectableImpl);
    }

    @Test
    public void register_objects_which_implement_Bindable() throws Exception {
        MerlinConnector<Bindable> onBinder = mock(MerlinConnector.class);
        Merlin merlin = new Merlin(null, null, null, onBinder);
        BindableImpl disconnectableImpl = new BindableImpl();

        merlin.registerBindable(disconnectableImpl);

        verify(onBinder).register(disconnectableImpl);
    }

    @Ignore("Need to make a helper class which takes object which can be registered")
    @Test
    public void not_register_objects_which_do_not_implement_reconnectable() throws Exception {
        NonRegisterableClass nonReconnectableClass = new NonRegisterableClass();

//        merlin.registerConnectable(nonReconnectableClass);

//        verify(merlinConnector, never()).register(any(Connectable.class));
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
