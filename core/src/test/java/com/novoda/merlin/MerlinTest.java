package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.Registerer;
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
    private Registerer registerer;

    private Merlin merlin;

    @Before
    public void setUp() {
        merlin = new Merlin(serviceBinder, registerer);
    }

    @Test
    public void whenBindingWithACustomEndpoint_thenSetsEndPoint() {
        String hostname = "startTheMerlinServiceWithAHostnameWhenProvided";

        merlin.setEndpoint(hostname, validator);
        merlin.bind();

        verify(serviceBinder).setEndpoint(hostname, validator);
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
    public void whenRegisteringConnectable_thenRegistersConnectable() {
        Connectable connectable = mock(Connectable.class);

        merlin.registerConnectable(connectable);

        verify(registerer).registerConnectable(connectable);
    }

    @Test
    public void whenRegisteringDisconnectable_thenRegistersDisconnectable() {
        Disconnectable disconnectable = mock(Disconnectable.class);

        merlin.registerDisconnectable(disconnectable);

        verify(registerer).registerDisconnectable(disconnectable);
    }

    @Test
    public void whenRegisteringBindable_thenRegistersBindable() {
        Bindable bindable = mock(Bindable.class);

        merlin.registerBindable(bindable);

        verify(registerer).registerBindable(bindable);
    }

}
