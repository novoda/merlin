package com.novoda.merlin;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NetworkStatusRetrieverTest {

    private static boolean CONNECTED = true;
    private static boolean DISCONNECTED = false;

    private final MerlinsBeard merlinsBeards = mock(MerlinsBeard.class);
    private final EndpointPinger endpointPinger = mock(EndpointPinger.class);
    private final EndpointPinger.PingerCallback pingerCallback = mock(EndpointPinger.PingerCallback.class);

    private NetworkStatusRetriever networkStatusRetriever;

    @Before
    public void setUp() {
        networkStatusRetriever = new NetworkStatusRetriever(merlinsBeards);
    }

    @Test
    public void givenMerlinsBeardIsConnected_whenFetchingWithPing_thenPingsUsingHostPinger() {
        given(merlinsBeards.isConnected()).willReturn(CONNECTED);

        networkStatusRetriever.fetchWithPing(endpointPinger, pingerCallback);

        verify(endpointPinger).ping(pingerCallback);
    }

    @Test
    public void givenMerlinsBeardIsDisconnected_whenFetchingWithPing_thenCallsNoNetworkToPing() {
        given(merlinsBeards.isConnected()).willReturn(DISCONNECTED);

        networkStatusRetriever.fetchWithPing(endpointPinger, pingerCallback);

        verify(endpointPinger).noNetworkToPing(pingerCallback);
    }

    @Test
    public void givenMerlinsBeardIsConnected_whenGettingNetworkStatus_thenReturnsNetworkStatusAvailable() {
        given(merlinsBeards.isConnected()).willReturn(CONNECTED);

        NetworkStatus networkStatus = networkStatusRetriever.retrieveNetworkStatus();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void givenMerlinsBeardIsDisconnected_whenGettingNetworkStatus_thenReturnsNetworkStatusUnavailable() {
        given(merlinsBeards.isConnected()).willReturn(DISCONNECTED);

        NetworkStatus networkStatus = networkStatusRetriever.retrieveNetworkStatus();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newUnavailableInstance());
    }
}
