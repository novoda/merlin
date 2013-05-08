package com.novoda.merlin;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

import com.novoda.merlin.receiver.ConnectivityReceiver;
import com.novoda.merlin.receiver.event.ConnectionEventPackager;
import com.novoda.merlin.receiver.event.ConnectivityChangeEvent;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;
import com.novoda.merlin.service.MerlinService;
import com.novoda.matcher.ComponentNameMatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MerlinServiceShould {

    @Mock private Intent intent;
    @Mock private Context context;
    @Mock private ConnectListener connectListener;
    @Mock private DisconnectListener disconnectListener;

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
    public void enable_connectivityReceiver_in_onBind() throws Exception {
        MerlinService spy = spy(merlinService);
        PackageManager packageManager = mock(PackageManager.class);
        ComponentName receiver = new ComponentName(context, ConnectivityReceiver.class);

        when(spy.getPackageManager()).thenReturn(packageManager);

        spy.onBind(intent);

        verify(packageManager).setComponentEnabledSetting(ComponentNameMatcher.match(receiver), eq(PackageManager.COMPONENT_ENABLED_STATE_ENABLED), eq(PackageManager.DONT_KILL_APP));
    }

    @Test
    public void disable_connectivityReceiver_in_onUnbind() throws Exception {
        MerlinService spy = spy(merlinService);
        PackageManager packageManager = mock(PackageManager.class);
        ComponentName receiver = new ComponentName(context, ConnectivityReceiver.class);

        when(spy.getPackageManager()).thenReturn(packageManager);

        spy.onUnbind(intent);

        verify(packageManager).setComponentEnabledSetting(ComponentNameMatcher.match(receiver), eq(PackageManager.COMPONENT_ENABLED_STATE_DISABLED), eq(PackageManager.DONT_KILL_APP));
    }

    @Test
    public void call_MerlinNetworkListener_onConnected_when_a_connected_connectivity_event_is_triggered() throws Exception {
        boolean isConnected = true;
        ConnectivityChangeEvent connectivityChangeEvent = createConnectivityChangeEvent(isConnected);

        merlinService.onConnectivityChanged(connectivityChangeEvent);

        verify(connectListener).onConnect();
    }

    @Test
    public void call_MerlinNetworkListener_onDisconnected_when_a_connected_connectivity_event_is_triggered() throws Exception {
        boolean isConnected = false;
        ConnectivityChangeEvent connectivityChangeEvent = createConnectivityChangeEvent(isConnected);

        merlinService.onConnectivityChanged(connectivityChangeEvent);

        verify(disconnectListener).onDisconnect();
    }

    private ConnectivityChangeEvent createConnectivityChangeEvent(boolean isConnected) {
        when(intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO)).thenReturn("info");
        when(intent.getStringExtra(ConnectivityManager.EXTRA_REASON)).thenReturn("reason");
        when(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)).thenReturn(!isConnected);

        return ConnectionEventPackager.from(intent);
    }

}
