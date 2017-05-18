package com.novoda.merlin.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.novoda.merlin.Endpoint;
import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MerlinServiceTest {

    private static final boolean IS_CONNECTED = true;
    private static final boolean NOT_CONNECTED = false;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Intent intent;
    @Mock
    private Context context;
    @Mock
    private ConnectListener connectListener;
    @Mock
    private DisconnectListener disconnectListener;
    @Mock
    private HostPinger defaultPinger;
    @Mock
    private HostPinger customPinger;
    @Mock
    private NetworkStatusRetriever networkStatusRetriever;

    private MerlinService merlinService;

    @Before
    public void setUp() {
        merlinService = new MerlinService() {

            @Override
            public void unregisterReceiver(BroadcastReceiver receiver) {
            }

            @Override
            public void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent) {
                // TODO : See https://github.com/novoda/merlin/issues/112
                if (connectivityChangeEvent.isConnected()) {
                    connectListener.onConnect();
                } else {
                    disconnectListener.onDisconnect();
                }
            }
        };
    }

    @Test
    public void givenConnectedEvent_whenConnectivityChanges_thenCallsOnConnect() {
        ConnectivityChangeEvent connectivityChangeEvent = createConnectivityChangeEvent(IS_CONNECTED);

        merlinService.onConnectivityChanged(connectivityChangeEvent);

        verify(connectListener).onConnect();
    }

    @Test
    public void givenDisconnectedEvent_whenConnectivityChanges_thenCallsOnDisconnect() {
        ConnectivityChangeEvent connectivityChangeEvent = createConnectivityChangeEvent(NOT_CONNECTED);

        merlinService.onConnectivityChanged(connectivityChangeEvent);

        verify(disconnectListener).onDisconnect();
    }

    @Test
    public void givenConnectedEvent_whenConnectivityChanges_thenFetchesUsingDefaultPinger() {
        ContextWrapper contextWrapper = stubbedContextWrapper();

        MerlinService merlinService = buildStubbedMerlinService(contextWrapper, networkStatusRetriever, defaultPinger, customPinger);

        merlinService.onCreate();
        merlinService.onConnectivityChanged(createConnectivityChangeEvent(IS_CONNECTED));
        verify(networkStatusRetriever).fetchWithPing(defaultPinger);
    }

    @Test
    public void givenConnectedEvent_andCustomEndpoint_whenConnectivityChanges_thenFetchesUsingCustomPinger() {
        ContextWrapper contextWrapper = stubbedContextWrapper();

        MerlinService merlinService = buildStubbedMerlinService(contextWrapper, networkStatusRetriever, defaultPinger, customPinger);

        merlinService.onCreate();
        merlinService.setHostname(Endpoint.from("some new host name"), mock(ResponseCodeValidator.class));
        merlinService.onConnectivityChanged(createConnectivityChangeEvent(IS_CONNECTED));
        verify(networkStatusRetriever).fetchWithPing(customPinger);
    }

    private ContextWrapper stubbedContextWrapper() {
        Context context = mock(Context.class);
        ContextWrapper contextWrapper = mock(ContextWrapper.class);
        ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        given(contextWrapper.getApplicationContext()).willReturn(context);
        given(context.getSystemService(Context.CONNECTIVITY_SERVICE)).willReturn(connectivityManager);
        return contextWrapper;
    }

    private ConnectivityChangeEvent createConnectivityChangeEvent(boolean isConnected) {
        return ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(isConnected, "info", "reason");
    }

    private MerlinService buildStubbedMerlinService(
            ContextWrapper contextWrapper,
            NetworkStatusRetriever retriever,
            HostPinger defaultPinger,
            HostPinger customPinger
    ) {
        return new TestDoubleMerlinServiceWithStubbedBuilders(
                contextWrapper,
                retriever,
                defaultPinger,
                customPinger
        );
    }

}
