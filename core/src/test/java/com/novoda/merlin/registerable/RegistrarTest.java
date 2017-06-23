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

public class RegistrarTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Register<Connectable> connectables;
    @Mock
    private Register<Disconnectable> disconnectables;
    @Mock
    private Register<Bindable> bindables;

    private Registrar registrar;

    @Before
    public void setUp() {
        registrar = new Registrar(connectables, disconnectables, bindables);
    }

    @Test(expected = MerlinException.class)
    public void givenMissingRegister_whenRegisteringConnectable_thenThrowsDeveloperException() {
        registrar = new Registrar(null, null, null);

        Connectable connectable = mock(Connectable.class);
        registrar.registerConnectable(connectable);
    }

    @Test(expected = MerlinException.class)
    public void givenMissingRegister_thenRegisteringDisconnectable_thenThrowsDeveloperException() {
        registrar = new Registrar(null, null, null);

        Disconnectable disconnectable = mock(Disconnectable.class);
        registrar.registerDisconnectable(disconnectable);
    }

    @Test(expected = MerlinException.class)
    public void givenMissingRegister_whenRegisteringBindable_thenThrowsDeveloperException() {
        registrar = new Registrar(null, null, null);

        Bindable bindable = mock(Bindable.class);
        registrar.registerBindable(bindable);
    }

    @Test
    public void givenRegister_whenRegisteringConnectable_thenRegistersConnectableWithConnector() {
        Connectable connectable = mock(Connectable.class);

        registrar.registerConnectable(connectable);

        verify(connectables).register(connectable);
    }

    @Test
    public void givenRegister_whenRegisteringDisconnectable_thenRegistersDisconnectableWithDisconnector() {
        Disconnectable disconnectable = mock(Disconnectable.class);

        registrar.registerDisconnectable(disconnectable);

        verify(this.disconnectables).register(disconnectable);
    }

    @Test
    public void givenRegister_whenRegisteringBindable_thenRegistersBindableWithBinder() {
        Bindable bindable = mock(Bindable.class);

        registrar.registerBindable(bindable);

        verify(bindables).register(bindable);
    }

    @Test
    public void whenClearingRegistrations_thenDelegatesClearingToRegisters() {
        registrar.clearRegistrations();

        verify(connectables).clear();
        verify(disconnectables).clear();
        verify(bindables).clear();
    }

}
