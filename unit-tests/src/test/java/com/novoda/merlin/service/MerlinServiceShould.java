package com.novoda.merlin.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.novoda.matcher.ComponentNameMatcher;
import com.novoda.merlin.MerlinRobolectricTestRunner;
import com.novoda.merlin.receiver.ConnectivityReceiver;
import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MerlinRobolectricTestRunner.class)
public class MerlinServiceShould {

    @Mock
    private Intent intent;
    @Mock
    private Context context;
    @Mock
    private ConnectListener connectListener;
    @Mock
    private DisconnectListener disconnectListener;

    private MerlinService merlinService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        merlinService = new MerlinService() {

            @Override
            public void unregisterReceiver(BroadcastReceiver receiver) {
            }

            @Override
            public void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent) {
                // TODO : this is bad, but I blame not having a proper constructor
                if (connectivityChangeEvent.isConnected()) {
                    connectListener.onConnect();
                } else {
                    disconnectListener.onDisconnect();
                }
            }
        };
    }

    @Test
    public void enableConnectivityReceiverInOnBind() throws Exception {
        MerlinService spy = spy(merlinService);
        PackageManager packageManager = mock(PackageManager.class);
        ComponentName receiver = new ComponentName(context, ConnectivityReceiver.class);

        when(spy.getPackageManager()).thenReturn(packageManager);

        spy.onBind(intent);

        verify(packageManager).setComponentEnabledSetting(ComponentNameMatcher.match(receiver), eq(PackageManager.COMPONENT_ENABLED_STATE_ENABLED), eq(PackageManager.DONT_KILL_APP));
    }

    @Test
    public void disableConnectivityReceiverInOnUnbind() throws Exception {
        MerlinService spy = spy(merlinService);
        PackageManager packageManager = mock(PackageManager.class);
        ComponentName receiver = new ComponentName(context, ConnectivityReceiver.class);

        when(spy.getPackageManager()).thenReturn(packageManager);

        spy.onUnbind(intent);

        verify(packageManager).setComponentEnabledSetting(ComponentNameMatcher.match(receiver), eq(PackageManager.COMPONENT_ENABLED_STATE_DISABLED), eq(PackageManager.DONT_KILL_APP));
    }

    @Test
    public void callMerlinNetworkListenerOnConnectedWhenAConnectedConnectivityEventIsTriggered() throws Exception {
        boolean isConnected = true;
        ConnectivityChangeEvent connectivityChangeEvent = createConnectivityChangeEvent(isConnected);

        merlinService.onConnectivityChanged(connectivityChangeEvent);

        verify(connectListener).onConnect();
    }

    @Test
    public void callMerlinNetworkListenerOnDisconnectedWhenAConnectedConnectivityEventIsTriggered() throws Exception {
        boolean isConnected = false;
        ConnectivityChangeEvent connectivityChangeEvent = createConnectivityChangeEvent(isConnected);

        merlinService.onConnectivityChanged(connectivityChangeEvent);

        verify(disconnectListener).onDisconnect();
    }

    private ConnectivityChangeEvent createConnectivityChangeEvent(boolean isConnected) {
        return new ConnectivityChangeEvent(isConnected, "info", "reason");
    }

}
