package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.novoda.merlin.service.ConnectivityChangeEventExtractor;
import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ConnectivityCallbacksTest {

    private static final ConnectivityChangeEvent ANY_CONNECTIVITY_CHANGE_EVENT = ConnectivityChangeEvent.createWithoutConnection();

    private static final boolean CONNECTED = true;
    private static final boolean DISCONNECTED = false;

    private static final String ANY_REASON = "reason";
    private static final String ANY_EXTRA_INFO = "extra info";

    private static final int MAX_MS_TO_LIVE = 0;

    private static final Network MISSING_NETWORK = null;
    private static final boolean CAN_NOTIFY = true;
    private static final boolean CANNOT_NOTIFY = false;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier;
    @Mock
    private Network network;
    @Mock
    private ConnectivityChangeEventExtractor extractor;

    private ConnectivityCallbacks networkCallbacks;

    @Before
    public void setUp() {
        given(connectivityChangesNotifier.canNotify()).willReturn(CAN_NOTIFY);
        given(extractor.extractFrom(any(Network.class))).willReturn(ANY_CONNECTIVITY_CHANGE_EVENT);

        networkCallbacks = new ConnectivityCallbacks(connectivityChangesNotifier, extractor);
    }

    @Test
    public void givenConnectedNetworkInfo_whenNetworkIsAvailable_thenNotifiesOfConnectedNetwork() {
        NetworkInfo connectedNetworkInfo = givenNetworkInfoWith(CONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onAvailable(network);

        thenNotifies();
    }

    @Test
    public void givenDisconnectedNetworkInfo_whenLosingNetwork_thenNotifiesOfDisconnectedNetwork() {
        NetworkInfo disconnectedNetworkInfo = givenNetworkInfoWith(DISCONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onLosing(network, MAX_MS_TO_LIVE);

        thenNotifies();
    }

    @Test
    public void givenDisconnectedNetworkInfo_whenNetworkIsLost_thenNotifiesOfLostNetwork() {
        NetworkInfo disconnectedNetworkInfo = givenNetworkInfoWith(DISCONNECTED, ANY_REASON, ANY_EXTRA_INFO);

        networkCallbacks.onLost(network);

        thenNotifies();
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

    private void thenNotifies() {
        ArgumentCaptor<ConnectivityChangeEvent> argumentCaptor = ArgumentCaptor.forClass(ConnectivityChangeEvent.class);
        verify(connectivityChangesNotifier).notify(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(ANY_CONNECTIVITY_CHANGE_EVENT);
    }

}
