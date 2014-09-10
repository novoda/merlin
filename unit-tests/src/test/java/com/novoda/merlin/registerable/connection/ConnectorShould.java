package com.novoda.merlin.registerable.connection;

import com.novoda.merlin.MerlinRobolectricTestRunner;
import com.novoda.merlin.registerable.MerlinConnector;
import com.novoda.merlin.registerable.MerlinRegisterer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MerlinRobolectricTestRunner.class)
public class ConnectorShould {

    private MerlinConnector<Connectable> merlinConnector;

    private Connector connector;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        merlinConnector = new MerlinRegisterer<Connectable>();
        connector = new Connector(merlinConnector);
    }

    @Test
    public void callbackRegisteredConnectables() throws Exception {
        Connectable connectable = mock(Connectable.class);
        merlinConnector.register(connectable);

        connector.onConnect();

        verify(connectable).onConnect();
    }

    @Test
    public void callbackAllRegistered() throws Exception {
        List<Connectable> connectables = createListOfConnectables();

        registerListOfConnectables(connectables);

        connector.onConnect();

        for (Connectable connectable : connectables) {
            verify(connectable).onConnect();
        }
    }

    private List<Connectable> createListOfConnectables() {
        List<Connectable> connectables = new ArrayList<Connectable>();
        initListOfConnectables(connectables);
        return connectables;
    }

    private void initListOfConnectables(List<Connectable> connectables) {
        for (int i = 0; i < 3; i++) {
            connectables.add(mock(Connectable.class));
        }
    }

    private void registerListOfConnectables(List<Connectable> connectables) {
        for (Connectable connectable : connectables) {
            merlinConnector.register(connectable);
        }
    }

}
