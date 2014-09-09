package com.novoda.merlin.registerable.disconnection;

import com.novoda.merlin.registerable.MerlinConnector;
import com.novoda.merlin.registerable.MerlinRegisterer;

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
    public void callback_registered_disconnectables() throws Exception {
        Disconnectable disconnectable = mock(Disconnectable.class);
        merlinConnector.register(disconnectable);

        disconnector.onDisconnect();

        verify(disconnectable).onDisconnect();
    }
}