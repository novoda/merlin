package com.novoda.merlin.service;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkStatusRetrieverShould {

    @Mock
    private MerlinsBeard mockMerlinsBeards;

    @Mock
    private HostPinger mockHostPinger;

    private NetworkStatusRetriever networkStatusRetriever;

    @Before
    public void setUp() {
        initMocks(this);
        networkStatusRetriever = new NetworkStatusRetriever(mockMerlinsBeards);
    }

    @Test
    public void whenNetworkIsConnectedHostPingerPings() {
        when(mockMerlinsBeards.isConnected()).thenReturn(true);

        networkStatusRetriever.fetchWithPing(mockHostPinger);

        verify(mockHostPinger).ping();
    }

    @Test
    public void whenNetworkIsDisconnectedHostPingerPerformsNoNetworkToPing() {
        when(mockMerlinsBeards.isConnected()).thenReturn(false);

        networkStatusRetriever.fetchWithPing(mockHostPinger);

        verify(mockHostPinger).noNetworkToPing();
    }

    @Test
    public void whenNetworkIsConnectedGetWithoutPingReturnsNetworkStatusAvailable() {
        when(mockMerlinsBeards.isConnected()).thenReturn(true);

        NetworkStatus networkStatus = networkStatusRetriever.get();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void whenNetworkIsDisconnectedGetWithoutPingReturnsNetworkStatusUnavailable() {
        when(mockMerlinsBeards.isConnected()).thenReturn(false);

        NetworkStatus networkStatus = networkStatusRetriever.get();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newUnavailableInstance());
    }
}
