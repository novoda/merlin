package com.novoda.merlin;

import android.content.Intent;
import android.net.ConnectivityManager;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ConnectivityChangeEventCreatorTest {

    private static final boolean CONNECTED = true;
    private static final boolean DISCONNECTED = false;

    private static final String ANY_INFO = "any_info";
    private static final String ANY_REASON = "any_reason";

    private final MerlinsBeard merlinsBeard = mock(MerlinsBeard.class);
    private final Intent intent = mock(Intent.class);

    private ConnectivityChangeEventCreator creator;

    @Before
    public void setUp() {
        creator = new ConnectivityChangeEventCreator();
        given(intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO)).willReturn(ANY_INFO);
        given(intent.getStringExtra(ConnectivityManager.EXTRA_REASON)).willReturn(ANY_REASON);
    }

    @Test
    public void givenIntentWithNoConnectivityOfConnected_whenCreatingAConnectivityChangeEvent_thenReturnsConnectedConnectivityChangeEvent() {
        givenIntentWithNoConnectivityExtraOf(CONNECTED);

        ConnectivityChangeEvent connectivityChangeEvent = creator.createFrom(intent, merlinsBeard);

        ConnectivityChangeEvent expectedConnectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                CONNECTED,
                ANY_INFO,
                ANY_REASON
        );
        assertThat(connectivityChangeEvent).isEqualTo(expectedConnectivityChangeEvent);
    }

    @Test
    public void givenIntentWithNoConnectivityOfDisconnected_whenCreatingAConnectivityChangeEvent_thenReturnsDisconnectedConnectivityChangeEvent() {
        givenIntentWithNoConnectivityExtraOf(DISCONNECTED);

        ConnectivityChangeEvent connectivityChangeEvent = creator.createFrom(intent, merlinsBeard);

        ConnectivityChangeEvent expectedConnectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                DISCONNECTED,
                ANY_INFO,
                ANY_REASON
        );
        assertThat(connectivityChangeEvent).isEqualTo(expectedConnectivityChangeEvent);
    }

    @Test
    public void givenMerlinsBeardIsConnected_whenCreatingAConnectivityChangeEvent_thenReturnsConnectedConnectivityChangeEvent() {
        givenMerlinsBeardIs(CONNECTED);

        ConnectivityChangeEvent connectivityChangeEvent = creator.createFrom(intent, merlinsBeard);

        ConnectivityChangeEvent expectedConnectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                CONNECTED,
                ANY_INFO,
                ANY_REASON
        );
        assertThat(connectivityChangeEvent).isEqualTo(expectedConnectivityChangeEvent);
    }

    @Test
    public void givenMerlinsBeardIsDisconnected_whenCreatingAConnectivityChangeEvent_thenReturnsDisconnectedConnectivityChangeEvent() {
        givenMerlinsBeardIs(DISCONNECTED);

        ConnectivityChangeEvent connectivityChangeEvent = creator.createFrom(intent, merlinsBeard);

        ConnectivityChangeEvent expectedConnectivityChangeEvent = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                DISCONNECTED,
                ANY_INFO,
                ANY_REASON
        );
        assertThat(connectivityChangeEvent).isEqualTo(expectedConnectivityChangeEvent);
    }

    private void givenMerlinsBeardIs(boolean isConnected) {
        given(intent.hasExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY)).willReturn(false);
        given(merlinsBeard.isConnected()).willReturn(isConnected);
    }

    private void givenIntentWithNoConnectivityExtraOf(boolean isConnected) {
        given(intent.hasExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY)).willReturn(true);
        given(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)).willReturn(!isConnected);
    }

}
