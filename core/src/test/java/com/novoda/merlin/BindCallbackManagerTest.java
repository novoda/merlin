package com.novoda.merlin;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BindCallbackManagerTest {

    private final NetworkStatus networkStatus = mock(NetworkStatus.class);
    private final Register<Bindable> bindables = new Register<>();

    private BindCallbackManager bindCallbackManager;

    @Before
    public void setUp() {
        bindCallbackManager = new BindCallbackManager(bindables);
    }

    @Test
    public void givenRegisteredConnectable_whenCallingOnConnect_thenCallsConnectForConnectable() {
        Bindable bindable = givenRegisteredBindable();

        bindCallbackManager.onMerlinBind(networkStatus);

        verify(bindable).onBind(networkStatus);
    }

    @Test
    public void givenMultipleRegisteredConnectables_whenCallingOnConnect_thenCallsConnectForAllConnectables() {
        List<Bindable> bindables = givenMultipleRegisteredBindables();

        bindCallbackManager.onMerlinBind(networkStatus);

        for (Bindable bindable : bindables) {
            verify(bindable).onBind(networkStatus);
        }
    }

    private List<Bindable> givenMultipleRegisteredBindables() {
        List<Bindable> bindables = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Bindable connectable = mock(Bindable.class);
            bindables.add(connectable);
            this.bindables.register(connectable);
        }
        return bindables;
    }

    private Bindable givenRegisteredBindable() {
        Bindable bindable = mock(Bindable.class);
        bindables.register(bindable);
        return bindable;
    }

}
