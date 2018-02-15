package com.novoda.merlin;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MerlinTest {

    private final MerlinServiceBinder serviceBinder = mock(MerlinServiceBinder.class);
    private final ResponseCodeValidator validator = mock(ResponseCodeValidator.class);
    private final Registrar registrar = mock(Registrar.class);

    private Merlin merlin;

    @Before
    public void setUp() {
        merlin = new Merlin(serviceBinder, registrar);
    }

    @Test
    public void whenBindingWithACustomEndpoint_thenSetsEndPoint() {
        Endpoint endpoint = Endpoint.from("startTheMerlinServiceWithAHostnameWhenProvided");

        merlin.setEndpoint(endpoint, validator);
        merlin.bind();

        verify(serviceBinder).setEndpoint(endpoint, validator);
    }

    @Test
    public void whenBinding_thenBindsService() {
        merlin.bind();

        verify(serviceBinder).bindService();
    }

    @Test
    public void whenUnbinding_thenUnbindsService() {
        merlin.unbind();

        verify(serviceBinder).unbind();
    }

    @Test
    public void whenUnbinding_thenClearsRegistrations() {
        merlin.unbind();

        verify(registrar).clearRegistrations();
    }

    @Test
    public void whenRegisteringConnectable_thenRegistersConnectable() {
        Connectable connectable = mock(Connectable.class);

        merlin.registerConnectable(connectable);

        verify(registrar).registerConnectable(connectable);
    }

    @Test
    public void whenRegisteringDisconnectable_thenRegistersDisconnectable() {
        Disconnectable disconnectable = mock(Disconnectable.class);

        merlin.registerDisconnectable(disconnectable);

        verify(registrar).registerDisconnectable(disconnectable);
    }

    @Test
    public void whenRegisteringBindable_thenRegistersBindable() {
        Bindable bindable = mock(Bindable.class);

        merlin.registerBindable(bindable);

        verify(registrar).registerBindable(bindable);
    }

}
