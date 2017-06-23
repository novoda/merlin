package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.Registrar;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.service.MerlinServiceBinder;
import com.novoda.merlin.service.ResponseCodeValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MerlinTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Context context;
    @Mock
    private MerlinServiceBinder serviceBinder;
    @Mock
    private ResponseCodeValidator validator;
    @Mock
    private Registrar registrar;

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
