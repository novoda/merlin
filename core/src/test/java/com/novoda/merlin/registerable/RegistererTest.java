package com.novoda.merlin.registerable;

import com.novoda.merlin.MerlinException;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RegistererTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Register<Connectable> connector;
    @Mock
    private Register<Disconnectable> disconnector;
    @Mock
    private Register<Bindable> binder;

    private Registerer registerer;

    @Before
    public void setUp() {
        registerer = new Registerer(connector, disconnector, binder);
    }

    @Test(expected = MerlinException.class)
    public void givenMissingConnector_whenRegisteringConnectable_thenThrowsDeveloperException() {
        registerer = new Registerer(null, null, null);

        Connectable connectable = mock(Connectable.class);
        registerer.registerConnectable(connectable);
    }

    @Test(expected = MerlinException.class)
    public void givenMissingDisconnector_thenRegisteringDisconnectable_thenThrowsDeveloperException() {
        registerer = new Registerer(null, null, null);

        Disconnectable disconnectable = mock(Disconnectable.class);
        registerer.registerDisconnectable(disconnectable);
    }

    @Test(expected = MerlinException.class)
    public void givenMissingBinder_whenRegisteringBindable_thenThrowsDeveloperException() {
        registerer = new Registerer(null, null, null);

        Bindable bindable = mock(Bindable.class);
        registerer.registerBindable(bindable);
    }

    @Test
    public void givenConnector_whenRegisteringConnectable_thenRegistersConnectableWithConnector() {
        Connectable connectable = mock(Connectable.class);

        registerer.registerConnectable(connectable);

        verify(connector).register(connectable);
    }

    @Test
    public void givenDisconnector_whenRegisteringDisconnectable_thenRegistersDisconnectableWithDisconnector() {
        Disconnectable disconnectable = mock(Disconnectable.class);

        registerer.registerDisconnectable(disconnectable);

        verify(disconnector).register(disconnectable);
    }

    @Test
    public void givenBinder_whenRegisteringBindable_thenRegistersBindableWithBinder() {
        Bindable bindable = mock(Bindable.class);

        registerer.registerBindable(bindable);

        verify(binder).register(bindable);
    }

}
