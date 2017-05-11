package com.novoda.merlin.service;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class NetworkStatusRetrieverTest {

    private static boolean CONNECTED = true;
    private static boolean DISCONNECTED = false;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MerlinsBeard merlinsBeards;
    @Mock
    private HostPinger hostPinger;

    private NetworkStatusRetriever networkStatusRetriever;

    @Before
    public void setUp() {
        networkStatusRetriever = new NetworkStatusRetriever(merlinsBeards);
    }

    @Test
    public void givenMerlinsBeardIsConnected_whenFetchingWithPing_thenPingsUsingHostPinger() {
        given(merlinsBeards.isConnected()).willReturn(CONNECTED);

        networkStatusRetriever.fetchWithPing(hostPinger);

        verify(hostPinger).ping();
    }

    @Test
    public void givenMerlinsBeardIsDisconnected_whenFetchingWithPing_thenCallsNoNetworkToPing() {
        given(merlinsBeards.isConnected()).willReturn(DISCONNECTED);

        networkStatusRetriever.fetchWithPing(hostPinger);

        verify(hostPinger).noNetworkToPing();
    }

    @Test
    public void givenMerlinsBeardIsConnected_whenGettingNetworkStatus_thenReturnsNetworkStatusAvailable() {
        given(merlinsBeards.isConnected()).willReturn(CONNECTED);

        NetworkStatus networkStatus = networkStatusRetriever.get();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void givenMerlinsBeardIsDisconnected_whenGettingNetworkStatus_thenReturnsNetworkStatusUnavailable() {
        given(merlinsBeards.isConnected()).willReturn(DISCONNECTED);

        NetworkStatus networkStatus = networkStatusRetriever.get();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newUnavailableInstance());
    }
}
