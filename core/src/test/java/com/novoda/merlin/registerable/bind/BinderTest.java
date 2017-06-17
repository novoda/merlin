package com.novoda.merlin.registerable.bind;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.CallbacksRegister;
import com.novoda.merlin.registerable.MerlinRegisterer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BinderTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private NetworkStatus networkStatus;

    private CallbacksRegister<Bindable> merlinBinder;

    private Binder binder;

    @Before
    public void setUp() {
        merlinBinder = new MerlinRegisterer<>();
        binder = new Binder(merlinBinder);
    }

    @Test
    public void givenRegisteredConnectable_whenCallingOnConnect_thenCallsConnectForConnectable() {
        Bindable bindable = givenRegisteredBindable();

        binder.onMerlinBind(networkStatus);

        Mockito.verify(bindable).onBind(networkStatus);
    }

    @Test
    public void givenMultipleRegisteredConnectables_whenCallingOnConnect_thenCallsConnectForAllConnectables() {
        List<Bindable> bindables = givenMultipleRegisteredBindables();

        binder.onMerlinBind(networkStatus);

        for (Bindable bindable : bindables) {
            Mockito.verify(bindable).onBind(networkStatus);
        }
    }

    private List<Bindable> givenMultipleRegisteredBindables() {
        List<Bindable> bindables = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Bindable connectable = Mockito.mock(Bindable.class);
            bindables.add(connectable);
            merlinBinder.register(connectable);
        }
        return bindables;
    }

    private Bindable givenRegisteredBindable() {
        Bindable bindable = Mockito.mock(Bindable.class);
        merlinBinder.register(bindable);
        return bindable;
    }

}
