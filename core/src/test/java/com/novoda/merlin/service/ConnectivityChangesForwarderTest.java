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
import static org.mockito.Mockito.never;
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
    public void givenPreviousConnectedNetworkStatus_whenNotifyingOfInitialState_thenCallsBindListenerWithNetworkStatus() {
        givenPreviousNetworkStatusIs(CONNECTED);

        connectivityChangesForwarder.forwardInitialNetworkStatus();

        verify(bindListener).onMerlinBind(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void givenPreviousDisconnectedNetworkStatus_whenNotifyingOfInitialState_thenCallsBindListenerWithNetworkStatus() {
        givenPreviousNetworkStatusIs(DISCONNECTED);

        connectivityChangesForwarder.forwardInitialNetworkStatus();

        verify(bindListener).onMerlinBind(NetworkStatus.newUnavailableInstance());
    }

    @Test
    public void givenNoPreviousNetworkStatus_whenNotifyingOfInitialState_thenCallsBindListenerWithNetworkStatusFromRetriever() {
        given(networkStatusRetriever.retrieveNetworkStatus()).willReturn(NetworkStatus.newAvailableInstance());

        connectivityChangesForwarder.forwardInitialNetworkStatus();

        verify(bindListener).onMerlinBind(NetworkStatus.newAvailableInstance());
    }

    @Test
    public void givenConnectivityChangeEvent_whenNotifyingOfConnectivityChangeEvent_thenFetchesWithPing() {
        ConnectivityChangeEvent connectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(CONNECTED, ANY_INFO, ANY_REASON);

        connectivityChangesForwarder.forward(connectivityChangeEvent);

        verify(networkStatusRetriever).fetchWithPing(eq(hostPinger), any(HostPinger.PingerCallback.class));
    }

    @Test
    public void givenFetchingWithPing_whenSuccessful_thenCallsOnConnect() {
        HostPinger.PingerCallback pingerCallback = givenFetchingWithPing();

        pingerCallback.onSuccess();

        verify(connectListener).onConnect();
    }

    @Test
    public void givenFetchingWithPing_whenUnsuccessful_thenCallsOnDisconnect() {
        HostPinger.PingerCallback pingerCallback = givenFetchingWithPing();

        pingerCallback.onFailure();

        verify(disconnectListener).onDisconnect();
    }

    @Test
    public void givenFetchingWithPing_butNullConnectListener_whenSuccessful_thenNeverCallsOnConnect() {
        connectivityChangesForwarder = new ConnectivityChangesForwarder(
                networkStatusRetriever, disconnectListener, null, bindListener, hostPinger
        );
        HostPinger.PingerCallback pingerCallback = givenFetchingWithPing();

        pingerCallback.onSuccess();

        verify(connectListener, never()).onConnect();
    }

    @Test
    public void givenFetchingWithPing_butNullDisconnectListener_whenUnsuccessful_thenNeverCallsOnDisconnect() {
        connectivityChangesForwarder = new ConnectivityChangesForwarder(
                networkStatusRetriever, null, connectListener, bindListener, hostPinger
        );
        HostPinger.PingerCallback pingerCallback = givenFetchingWithPing();

        pingerCallback.onFailure();

        verify(disconnectListener, never()).onDisconnect();
    }

    private HostPinger.PingerCallback givenFetchingWithPing() {
        ConnectivityChangeEvent connectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(CONNECTED, ANY_INFO, ANY_REASON);
        connectivityChangesForwarder.forward(connectivityChangeEvent);
        ArgumentCaptor<HostPinger.PingerCallback> argumentCaptor = ArgumentCaptor.forClass(HostPinger.PingerCallback.class);
        verify(networkStatusRetriever).fetchWithPing(eq(hostPinger), argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

    private void givenPreviousNetworkStatusIs(boolean connected) {
        connectivityChangesForwarder.forward(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(connected, ANY_INFO, ANY_REASON));
    }
}
