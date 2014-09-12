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

public class CurrentNetworkStatusFetcherShould {

    @Mock
    private MerlinsBeard mockMerlinsBeards;

    @Mock
    private HostPinger mockHostPinger;

    private CurrentNetworkStatusFetcher currentNetworkStatusFetcher;

    @Before
    public void setUp() {
        initMocks(this);
        currentNetworkStatusFetcher = new CurrentNetworkStatusFetcher(mockMerlinsBeards, mockHostPinger);
    }

    @Test
    public void getPerformsHostPingerPingWhenIsConnectedReturnsTrue() {
        when(mockMerlinsBeards.isConnected()).thenReturn(true);

        currentNetworkStatusFetcher.get();

        verify(mockHostPinger).ping();
    }

    @Test
    public void getPerformsHostPingerNoNetworkToPingWhenIsConnectedReturnsFalse() {
        when(mockMerlinsBeards.isConnected()).thenReturn(false);

        currentNetworkStatusFetcher.get();

        verify(mockHostPinger).noNetworkToPing();
    }

    @Test
    public void getWithoutPingReturnsNetworkStatusAvailableWhenIsConnectedReturnsTrue(){
        when(mockMerlinsBeards.isConnected()).thenReturn(true);

        NetworkStatus networkStatus = currentNetworkStatusFetcher.getWithoutPing();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void getWithoutPingReturnsNetworkStatusUnavailableWhenIsConnectedReturnsFalse(){
        when(mockMerlinsBeards.isConnected()).thenReturn(false);

        NetworkStatus networkStatus = currentNetworkStatusFetcher.getWithoutPing();

        assertThat(networkStatus).isEqualTo(NetworkStatus.newUnavailableInstance());
    }
}
