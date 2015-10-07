package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.registerable.MerlinConnector;
import com.novoda.merlin.registerable.MerlinRegisterer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DisconnectorShould {

    private MerlinConnector<Disconnectable> merlinConnector;

    private Disconnector disconnector;

    @Before
    public void setUp() {
        initMocks(this);
        merlinConnector = new MerlinRegisterer<Disconnectable>();
        disconnector = new Disconnector(merlinConnector);
    }

    @Test
    public void callback_registered_disconnectable() throws Exception {
        Disconnectable disconnectable = mock(Disconnectable.class);
        merlinConnector.register(disconnectable);

        disconnector.onDisconnect();

        verify(disconnectable).onDisconnect();
    }

    @Test
    public void callback_registered_disconnectables() throws Exception {
        List<Disconnectable> disconnectables = new ArrayList<Disconnectable>(3);
        init_disconnectables_list(disconnectables);
        register_disconnectables_list(disconnectables);

        disconnector.onDisconnect();

        for (Disconnectable disconnectable : disconnectables) {
            verify(disconnectable).onDisconnect();
        }
    }

    private void init_disconnectables_list(List<Disconnectable> disconnectables) {
        for (int disconnectableIndex = 0; disconnectableIndex < disconnectables.size(); disconnectableIndex++) {
            disconnectables.add(mock(Disconnectable.class));
        }
    }

    private void register_disconnectables_list(List<Disconnectable> disconnectables) {
        for (Disconnectable disconnectable : disconnectables) {
            merlinConnector.register(disconnectable);
        }
    }

}