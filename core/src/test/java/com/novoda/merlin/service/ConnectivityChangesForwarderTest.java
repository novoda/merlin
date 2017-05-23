package com.novoda.merlin.service;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ConnectivityChangesForwarderTest {

    private static final boolean CONNECTED = true;
    private static final boolean DISCONNECTED = false;
    private static final String ANY_INFO = "any_info";
    private static final String ANY_REASON = "any_reason";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private NetworkStatusRetriever networkStatusRetriever;
    @Mock
    private DisconnectListener disconnectListener;
    @Mock
    private ConnectListener connectListener;
    @Mock
    private BindListener bindListener;
    @Mock
    private HostPinger hostPinger;

    private ConnectivityChangesForwarder connectivityChangesForwarder;

    @Before
    public void setUp() {
        connectivityChangesForwarder = new ConnectivityChangesForwarder(
                networkStatusRetriever,
                disconnectListener,
                connectListener,
                bindListener,
                hostPinger
        );
    }

    @Test
    public void givenPreviousNetworkStatus_whenNotifyingOfInitialState_thenCallsBindListenerWithNetworkStatus() {
        givenPreviousNetworkStatusIs(CONNECTED);

        connectivityChangesForwarder.notifyOfInitialNetworkStatus();

        verify(bindListener).onMerlinBind(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void givenNoPreviousNetworkStatus_whenNotifyingOfInitialState_thenCallsBindListenerWithNetworkStatusFromRetriever() {
        given(networkStatusRetriever.retrieveNetworkStatus()).willReturn(NetworkStatus.newAvailableInstance());

        connectivityChangesForwarder.notifyOfInitialNetworkStatus();

        verify(bindListener).onMerlinBind(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void givenConnectivityChangeEvent_whenNotifyingOfConnectivityChangeEvent_thenFetchesWithPing() {
        ConnectivityChangeEvent connectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(CONNECTED, ANY_INFO, ANY_REASON);

        connectivityChangesForwarder.notifyOf(connectivityChangeEvent);

        verify(networkStatusRetriever).fetchWithPing(eq(hostPinger), any(HostPinger.PingerCallback.class));
    }

    @Test
    public void givenSuccessfulPing_whenFetchingWithPing_thenCallsOnConnect() {
        HostPinger.PingerCallback pingerCallback = givenHostPingerCallback();
        pingerCallback.onSuccess();

        verify(connectListener).onConnect();
    }

    @Test
    public void givenUnsuccessfulPing_whenFetchingWithPing_thenCallsOnDisconnect() {
        HostPinger.PingerCallback pingerCallback = givenHostPingerCallback();
        pingerCallback.onFailure();

        verify(disconnectListener).onDisconnect();
    }

    private HostPinger.PingerCallback givenHostPingerCallback() {
        ConnectivityChangeEvent connectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(CONNECTED, ANY_INFO, ANY_REASON);
        connectivityChangesForwarder.notifyOf(connectivityChangeEvent);
        ArgumentCaptor<HostPinger.PingerCallback> argumentCaptor = ArgumentCaptor.forClass(HostPinger.PingerCallback.class);
        verify(networkStatusRetriever).fetchWithPing(eq(hostPinger), argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

    private void givenPreviousNetworkStatusIs(boolean connected) {
        connectivityChangesForwarder.notifyOf(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(connected, ANY_INFO, ANY_REASON));
    }
}
