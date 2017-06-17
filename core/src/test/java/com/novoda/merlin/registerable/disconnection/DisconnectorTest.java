package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.registerable.Register;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DisconnectorTest {

    private Register<Disconnectable> disconnectables;

    private Disconnector disconnector;

    @Before
    public void setUp() {
        initMocks(this);
        disconnectables = new Register<>();
        disconnector = new Disconnector(disconnectables);
    }

    @Test
    public void givenRegisteredDisconnectable_whenCallingOnDisconect_thenCallsDisconnectForDisconnectable() {
        Disconnectable disconnectable = givenRegisteredDisconnectable();

        disconnector.onDisconnect();

        verify(disconnectable).onDisconnect();
    }

    @Test
    public void givenMultipleRegisteredDisconnectables_whenCallingOnConnect_thenCallsConnectForAllDisconnectables() {
        List<Disconnectable> disconnectables = givenMultipleRegisteredDisconnectables();

        disconnector.onDisconnect();

        for (Disconnectable disconnectable : disconnectables) {
            verify(disconnectable).onDisconnect();
        }
    }

    private List<Disconnectable> givenMultipleRegisteredDisconnectables() {
        List<Disconnectable> disconnectables = new ArrayList<>(3);

        for (int disconnectableIndex = 0; disconnectableIndex < disconnectables.size(); disconnectableIndex++) {
            Disconnectable disconnectable = mock(Disconnectable.class);
            disconnectables.add(disconnectable);
            this.disconnectables.register(disconnectable);
        }

        return disconnectables;
    }

    private Disconnectable givenRegisteredDisconnectable() {
        Disconnectable disconnectable = mock(Disconnectable.class);
        disconnectables.register(disconnectable);
        return disconnectable;
    }

}
