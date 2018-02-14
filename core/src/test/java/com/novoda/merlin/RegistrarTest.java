package com.novoda.merlin;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class RegistrarTest {

    private final Register<Connectable> connectables = mock(Register.class);
    private final Register<Disconnectable> disconnectables = mock(Register.class);
    private final Register<Bindable> bindables = mock(Register.class);

    private Registrar registrar;

    @Before
    public void setUp() {
        registrar = new Registrar(connectables, disconnectables, bindables);
    }

    @Test(expected = IllegalStateException.class)
    public void givenMissingRegister_whenRegisteringConnectable_thenThrowsIllegalStateException() {
        registrar = new Registrar(null, null, null);

        Connectable connectable = mock(Connectable.class);
        registrar.registerConnectable(connectable);
    }

    @Test(expected = IllegalStateException.class)
    public void givenMissingRegister_thenRegisteringDisconnectable_thenThrowsIllegalStateException() {
        registrar = new Registrar(null, null, null);

        Disconnectable disconnectable = mock(Disconnectable.class);
        registrar.registerDisconnectable(disconnectable);
    }

    @Test(expected = IllegalStateException.class)
    public void givenMissingRegister_whenRegisteringBindable_thenThrowsIllegalStateException() {
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

    @Test
    public void givenMissingConnectables_whenClearingRegistrations_thenDoesNothing() {
        registrar = new Registrar(null, disconnectables, bindables);

        registrar.clearRegistrations();

        verifyZeroInteractions(connectables);
    }

    @Test
    public void givenMissingDisconnectables_whenClearingRegistrations_thenDoesNothing() {
        registrar = new Registrar(connectables, null, bindables);

        registrar.clearRegistrations();

        verifyZeroInteractions(disconnectables);
    }

    @Test
    public void givenMissingBindables_whenClearingRegistrations_thenDoesNothing() {
        registrar = new Registrar(connectables, disconnectables, null);

        registrar.clearRegistrations();

        verifyZeroInteractions(bindables);
    }

}
