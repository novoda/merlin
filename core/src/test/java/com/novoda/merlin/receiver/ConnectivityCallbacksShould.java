package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ConnectivityCallbacksShould {

    private static final boolean CONNECTED = true;
    private static final boolean DISCONNECTED = false;

    private static final String ANY_REASON = "reason";
    private static final String ANY_EXTRA_INFO = "extra info";

    private static final int MAX_MS_TO_LIVE = 0;

    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private MerlinService merlinService;
    @Mock
    private Network network;

    private ConnectivityCallbacks networkCallbacks;

    @Before
    public void setUp() {
        initMocks(this);
        networkCallbacks = new ConnectivityCallbacks(connectivityManager, merlinService);
    }

    @Test
    public void notifyMerlinServiceOfAvailableNetwork() {
        NetworkInfo networkInfo = givenNetworkInfoWith(CONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onAvailable(network);

        thenConnectivityChangeEventContains(networkInfo);
    }

    @Test
    public void notifyMerlinServiceOfLosingNetwork() {
        NetworkInfo networkInfo = givenNetworkInfoWith(DISCONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onLosing(network, MAX_MS_TO_LIVE);

        thenConnectivityChangeEventContains(networkInfo);
    }

    @Test
    public void notifyMerlinServiceOfLostNetwork() {
        NetworkInfo networkInfo = givenNetworkInfoWith(DISCONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onLost(network);

        thenConnectivityChangeEventContains(networkInfo);
    }

    private NetworkInfo givenNetworkInfoWith(boolean connected, String reason, String extraInfo) {
        NetworkInfo networkInfo = mock(NetworkInfo.class);

        when(networkInfo.isConnected()).thenReturn(connected);
        when(networkInfo.getReason()).thenReturn(reason);
        when(networkInfo.getExtraInfo()).thenReturn(extraInfo);
        when(connectivityManager.getNetworkInfo(network)).thenReturn(networkInfo);

        return networkInfo;
    }

    private void thenConnectivityChangeEventContains(NetworkInfo networkInfo) {
        ArgumentCaptor<ConnectivityChangeEvent> argumentCaptor = ArgumentCaptor.forClass(ConnectivityChangeEvent.class);
        verify(merlinService).onConnectivityChanged(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                networkInfo.isConnected(),
                networkInfo.getExtraInfo(),
                networkInfo.getReason()
        ));
    }

}
