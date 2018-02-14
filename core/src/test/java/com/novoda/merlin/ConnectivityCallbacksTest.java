package com.novoda.merlin;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ConnectivityCallbacksTest {

    private static final boolean CONNECTED = true;
    private static final boolean DISCONNECTED = false;

    private static final String ANY_REASON = "reason";
    private static final String ANY_EXTRA_INFO = "extra info";

    private static final int MAX_MS_TO_LIVE = 0;

    private static final Network MISSING_NETWORK = null;
    private static final boolean CAN_NOTIFY = true;
    private static final boolean CANNOT_NOTIFY = false;

    private final ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
    private final MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier = mock(MerlinService.ConnectivityChangesNotifier.class);
    private final Network network = mock(Network.class);

    private ConnectivityCallbacks networkCallbacks;

    @Before
    public void setUp() {
        given(connectivityChangesNotifier.canNotify()).willReturn(CAN_NOTIFY);
        ConnectivityChangeEventExtractor extractor = new ConnectivityChangeEventExtractor(connectivityManager);
        networkCallbacks = new ConnectivityCallbacks(connectivityChangesNotifier, extractor);
    }

    @Test
    public void givenConnectedNetworkInfo_whenNetworkIsAvailable_thenNotifiesOfConnectedNetwork() {
        NetworkInfo networkInfo = givenNetworkInfoWith(CONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onAvailable(network);

        verify(connectivityChangesNotifier).notify(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                networkInfo.isConnected(), networkInfo.getExtraInfo(), networkInfo.getReason()
        ));
    }

    @Test
    public void givenDisconnectedNetworkInfo_whenLosingNetwork_thenNotifiesOfDisconnectedNetwork() {
        NetworkInfo networkInfo = givenNetworkInfoWith(DISCONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onLosing(network, MAX_MS_TO_LIVE);

        verify(connectivityChangesNotifier).notify(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                networkInfo.isConnected(), networkInfo.getExtraInfo(), networkInfo.getReason()
        ));
    }

    @Test
    public void givenDisconnectedNetworkInfo_whenNetworkIsLost_thenNotifiesOfLostNetwork() {
        NetworkInfo networkInfo = givenNetworkInfoWith(DISCONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onLost(network);

        verify(connectivityChangesNotifier).notify(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                networkInfo.isConnected(), networkInfo.getExtraInfo(), networkInfo.getReason()
        ));
    }

    @Test
    public void givenNoNetworkInfo_whenNetworkIsAvailable_thenNotifiesOfConnectedNetwork() {
        networkCallbacks.onAvailable(network);

        verify(connectivityChangesNotifier).notify(ConnectivityChangeEvent.createWithoutConnection());
    }

    @Test
    public void givenNoNetworkInfo_whenLosingNetwork_thenNotifiesOfDisconnectedNetwork() {
        networkCallbacks.onLosing(network, MAX_MS_TO_LIVE);

        verify(connectivityChangesNotifier).notify(ConnectivityChangeEvent.createWithoutConnection());
    }

    @Test
    public void givenNoNetworkInfo_whenNetworkIsLost_thenNotifiesOfLostNetwork() {
        networkCallbacks.onLost(network);

        verify(connectivityChangesNotifier).notify(ConnectivityChangeEvent.createWithoutConnection());
    }

    @Test
    public void givenCannotNotify_whenNetworkIsAvailable_thenNeverNotifiesConnectivityChangeEvent() {
        given(connectivityChangesNotifier.canNotify()).willReturn(CANNOT_NOTIFY);

        networkCallbacks.onAvailable(MISSING_NETWORK);

        InOrder inOrder = inOrder(connectivityChangesNotifier);
        inOrder.verify(connectivityChangesNotifier).canNotify();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void givenCannotNotify_whenLosingNetwork_thenNeverNotifiesConnectivityChangeEvent() {
        given(connectivityChangesNotifier.canNotify()).willReturn(CANNOT_NOTIFY);

        networkCallbacks.onLosing(MISSING_NETWORK, MAX_MS_TO_LIVE);

        InOrder inOrder = inOrder(connectivityChangesNotifier);
        inOrder.verify(connectivityChangesNotifier).canNotify();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void givenCannotNotify_whenNetworkIsLost_thenNeverNotifiesConnectivityChangeEvent() {
        given(connectivityChangesNotifier.canNotify()).willReturn(CANNOT_NOTIFY);

        networkCallbacks.onLost(MISSING_NETWORK);

        InOrder inOrder = inOrder(connectivityChangesNotifier);
        inOrder.verify(connectivityChangesNotifier).canNotify();
        inOrder.verifyNoMoreInteractions();
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

}
