package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.MerlinConnector;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.service.MerlinServiceBinder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MerlinShould {

    @Mock private Context context;
    @Mock private MerlinServiceBinder serviceBinder;

    private Merlin merlin;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        merlin = new Merlin(serviceBinder);
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
    public void throw_a_developer_exception_when_a_MerlinReconnector_is_not_set_but_registerReconnectable_is_called() throws Exception {
        merlin = new Merlin(serviceBinder);
        Connectable connectable = mock(Connectable.class);

        merlin.registerConnectable(connectable);
    }

    @Test
    public void register_objects_which_implement_reconnectable() throws Exception {
        MerlinConnector<Connectable> connector = mock(MerlinConnector.class);
        Merlin merlin = new Merlin(null, connector, null);
        ConnectableClass reconnectableClass = new ConnectableClass();

        merlin.registerConnectable(reconnectableClass);

        verify(connector).register(reconnectableClass);
    }

    @Ignore("Need to make a helper class which takes object which can be registered")
    @Test
    public void not_register_objects_which_do_not_implement_reconnectable() throws Exception {
        NonReconnectableClass nonReconnectableClass = new NonReconnectableClass();

//        merlin.registerConnectable(nonReconnectableClass);

//        verify(merlinConnector, never()).register(any(Connectable.class));
    }

    private static class ConnectableClass implements Connectable {
        @Override
        public void onConnect() {
        }
    }

    private static class NonReconnectableClass {
    }

}
