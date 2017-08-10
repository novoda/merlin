package com.novoda.merlin.service;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.registerable.bind.BindCallbackManager;
import com.novoda.merlin.registerable.connection.ConnectCallbackManager;
import com.novoda.merlin.registerable.disconnection.DisconnectCallbackManager;

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
    private static final NetworkStatus AVAILABLE_NETWORK = NetworkStatus.newAvailableInstance();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private NetworkStatusRetriever networkStatusRetriever;
    @Mock
    private DisconnectCallbackManager disconnectCallbackManager;
    @Mock
    private ConnectCallbackManager connectCallbackManager;
    @Mock
    private BindCallbackManager bindCallbackManager;
    @Mock
    private EndpointPinger endpointPinger;

    private ConnectivityChangesForwarder connectivityChangesForwarder;

    @Before
    public void setUp() {
        connectivityChangesForwarder = new ConnectivityChangesForwarder(
                networkStatusRetriever,
                disconnectCallbackManager,
                connectCallbackManager,
                bindCallbackManager,
                endpointPinger
        );
    }

    @Test
    public void givenNetworkWasConnected_whenNotifyingOfInitialState_thenForwardsNetworkAvailableToListener() {
        givenNetworkWas(CONNECTED);

        connectivityChangesForwarder.forwardInitialNetworkStatus();

        verify(bindCallbackManager).onMerlinBind(AVAILABLE_NETWORK);
    }

    @Test
    public void givenNetworkWasDisconnected_whenNotifyingOfInitialState_thenForwardsNetworkUnavailableToListener() {
        givenNetworkWas(DISCONNECTED);

        connectivityChangesForwarder.forwardInitialNetworkStatus();

        verify(bindCallbackManager).onMerlinBind(NetworkStatus.newUnavailableInstance());
    }

    @Test
    public void givenEndpointPingHasNotOccurred_whenNotifyingOfInitialState_thenForwardsNetworkAvailableToListener() {
        given(networkStatusRetriever.retrieveNetworkStatus()).willReturn(AVAILABLE_NETWORK);

        connectivityChangesForwarder.forwardInitialNetworkStatus();

        verify(bindCallbackManager).onMerlinBind(AVAILABLE_NETWORK);
    }

    @Test
    public void givenConnectivityChangeEvent_whenNotifyingOfConnectivityChangeEvent_thenDelegatesRefreshingToRetriever() {
        ConnectivityChangeEvent connectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(CONNECTED, ANY_INFO, ANY_REASON);

        connectivityChangesForwarder.forward(connectivityChangeEvent);

        verify(networkStatusRetriever).fetchWithPing(eq(endpointPinger), any(EndpointPinger.PingerCallback.class));
    }

    @Test
    public void givenFetchingWithPing_whenSuccessful_thenCallsOnConnect() {
        EndpointPinger.PingerCallback pingerCallback = givenFetchingWithPing();

        pingerCallback.onSuccess();

        verify(connectCallbackManager).onConnect();
    }

    @Test
    public void givenFetchingWithPing_whenUnsuccessful_thenCallsOnDisconnect() {
        EndpointPinger.PingerCallback pingerCallback = givenFetchingWithPing();

        pingerCallback.onFailure();

        verify(disconnectCallbackManager).onDisconnect();
    }

    @Test
    public void givenNetworkWasConnected_andNullBindListener_whenNotifyingOfInitialState_thenForwardsNetworkAvailableToListener() {
        connectivityChangesForwarder = new ConnectivityChangesForwarder(
                networkStatusRetriever, disconnectCallbackManager, connectCallbackManager, null, endpointPinger
        );
        givenNetworkWas(CONNECTED);

        connectivityChangesForwarder.forwardInitialNetworkStatus();

        verify(bindCallbackManager, never()).onMerlinBind(any(NetworkStatus.class));
    }

    @Test
    public void givenEndpointPingHasNotOccurred_andNullBindListener_whenNotifyingOfInitialState_thenForwardsNetworkAvailableToListener() {
        connectivityChangesForwarder = new ConnectivityChangesForwarder(
                networkStatusRetriever, disconnectCallbackManager, connectCallbackManager, null, endpointPinger
        );
        given(networkStatusRetriever.retrieveNetworkStatus()).willReturn(AVAILABLE_NETWORK);

        connectivityChangesForwarder.forwardInitialNetworkStatus();

        verify(bindCallbackManager, never()).onMerlinBind(any(NetworkStatus.class));
    }

    @Test
    public void givenFetchingWithPing_andNullConnectListener_whenSuccessful_thenNeverCallsOnConnect() {
        connectivityChangesForwarder = new ConnectivityChangesForwarder(
                networkStatusRetriever, disconnectCallbackManager, null, bindCallbackManager, endpointPinger
        );
        EndpointPinger.PingerCallback pingerCallback = givenFetchingWithPing();

        pingerCallback.onSuccess();

        verify(connectCallbackManager, never()).onConnect();
    }

    @Test
    public void givenFetchingWithPing_andNullDisconnectListener_whenUnsuccessful_thenNeverCallsOnDisconnect() {
        connectivityChangesForwarder = new ConnectivityChangesForwarder(
                networkStatusRetriever, null, connectCallbackManager, bindCallbackManager, endpointPinger
        );
        EndpointPinger.PingerCallback pingerCallback = givenFetchingWithPing();

        pingerCallback.onFailure();

        verify(disconnectCallbackManager, never()).onDisconnect();
    }

    private void givenNetworkWas(boolean connected) {
        connectivityChangesForwarder.forward(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(connected, ANY_INFO, ANY_REASON));
    }

    private EndpointPinger.PingerCallback givenFetchingWithPing() {
        ConnectivityChangeEvent connectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(CONNECTED, ANY_INFO, ANY_REASON);
        connectivityChangesForwarder.forward(connectivityChangeEvent);
        ArgumentCaptor<EndpointPinger.PingerCallback> argumentCaptor = ArgumentCaptor.forClass(EndpointPinger.PingerCallback.class);
        verify(networkStatusRetriever).fetchWithPing(eq(endpointPinger), argumentCaptor.capture());
        return argumentCaptor.getValue();
    }
}
