package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectivityCallbacksTest {

    private static final boolean CONNECTED = true;
    private static final boolean DISCONNECTED = false;

    private static final String ANY_REASON = "reason";
    private static final String ANY_EXTRA_INFO = "extra info";

    private static final int MAX_MS_TO_LIVE = 0;

    private static final Network MISSING_NETWORK = null;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier;
    @Mock
    private Network network;

    private ConnectivityCallbacks networkCallbacks;

    @Before
    public void setUp() {
        networkCallbacks = new ConnectivityCallbacks(connectivityManager, connectivityChangesNotifier);
    }

    @Test
    public void givenConnectedNetworkInfo_whenNetworkIsAvailable_thenNotifiesMerlinServiceOfConnectedNetwork() {
        NetworkInfo connectedNetworkInfo = givenNetworkInfoWith(CONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onAvailable(network);

        thenNotifiesMerlinServiceOf(connectedNetworkInfo);
    }

    @Test
    public void givenDisconnectedNetworkInfo_whenLosingNetwork_thenNotifiesMerlinServiceOfDisconnectedNetwork() {
        NetworkInfo disconnectedNetworkInfo = givenNetworkInfoWith(DISCONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onLosing(network, MAX_MS_TO_LIVE);

        thenNotifiesMerlinServiceOf(disconnectedNetworkInfo);
    }

    @Test
    public void givenDisconnectedNetworkInfo_whenNetworkIsLost_thenNotifiesMerlinServiceOfLostNetwork() {
        NetworkInfo disconnectedNetworkInfo = givenNetworkInfoWith(DISCONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onLost(network);

        thenNotifiesMerlinServiceOf(disconnectedNetworkInfo);
    }

    @Test
    public void givenMissingNetwork_whenNetworkIsAvailable_thenNotifiesMerlinServiceOfMissingNetwork() {
        networkCallbacks.onAvailable(MISSING_NETWORK);

        thenNotifiesMerlinServiceOfMissingNetwork();
    }

    @Test
    public void givenMissingNetwork_whenLosingNetwork_thenNotifiesMerlinServiceOfMissingNetwork() {
        networkCallbacks.onLosing(MISSING_NETWORK, MAX_MS_TO_LIVE);

        thenNotifiesMerlinServiceOfMissingNetwork();
    }

    @Test
    public void givenMissingNetwork_whenNetworkIsLost_thenNotifiesMerlinServiceOfMissingNetwork() {
        networkCallbacks.onLost(MISSING_NETWORK);

        thenNotifiesMerlinServiceOfMissingNetwork();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private NetworkInfo givenNetworkInfoWith(boolean connected, String reason, String extraInfo) {
        NetworkInfo networkInfo = mock(NetworkInfo.class);

        given(networkInfo.isConnected()).willReturn(connected);
        given(networkInfo.getReason()).willReturn(reason);
        given(networkInfo.getExtraInfo()).willReturn(extraInfo);
        given(connectivityManager.getNetworkInfo(network)).willReturn(networkInfo);

        return networkInfo;
    }

    private void thenNotifiesMerlinServiceOf(NetworkInfo networkInfo) {
        ArgumentCaptor<ConnectivityChangeEvent> argumentCaptor = ArgumentCaptor.forClass(ConnectivityChangeEvent.class);
        verify(connectivityChangesNotifier).notify(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                networkInfo.isConnected(),
                networkInfo.getExtraInfo(),
                networkInfo.getReason()
        ));
    }

    private void thenNotifiesMerlinServiceOfMissingNetwork() {
        ArgumentCaptor<ConnectivityChangeEvent> argumentCaptor = ArgumentCaptor.forClass(ConnectivityChangeEvent.class);
        verify(connectivityChangesNotifier).notify(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(ConnectivityChangeEvent.createWithoutConnection());
    }

}
