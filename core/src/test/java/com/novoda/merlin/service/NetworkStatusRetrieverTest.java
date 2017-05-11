package com.novoda.merlin.service;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkStatusRetrieverTest {

    private static boolean CONNECTED = true;
    private static boolean DISCONNECTED = false;

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
    public void givenMerlinsBeardIsConnected_whenFetchingWithPing_thenPingsUsingHostPinger() {
        given(mockMerlinsBeards.isConnected()).willReturn(CONNECTED);

        networkStatusRetriever.fetchWithPing(mockHostPinger);

        verify(mockHostPinger).ping();
    }

    @Test
    public void givenMerlinsBeardIsDisconnected_whenFetchingWithPing_thenCallsNoNetworkToPing() {
        given(mockMerlinsBeards.isConnected()).willReturn(DISCONNECTED);

        networkStatusRetriever.fetchWithPing(mockHostPinger);

        verify(mockHostPinger).noNetworkToPing();
    }

    @Test
    public void givenMerlinsBeardIsConnected_whenGettingNetworkStatus_thenReturnsNetworkStatusAvailable() {
        given(mockMerlinsBeards.isConnected()).willReturn(CONNECTED);

        NetworkStatus networkStatus = networkStatusRetriever.get();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void givenMerlinsBeardIsDisconnected_whenGettingNetworkStatus_thenReturnsNetworkStatusUnavailable() {
        given(mockMerlinsBeards.isConnected()).willReturn(DISCONNECTED);

        NetworkStatus networkStatus = networkStatusRetriever.get();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newUnavailableInstance());
    }
}
