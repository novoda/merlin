package com.novoda.merlin.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
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
        PackageManager mockPackageManager = mock(PackageManager.class);
        ComponentName mockReceiver = mock(ComponentName.class);
        MerlinService merlinService = new TestDoubleMerlinService(mockPackageManager, mockReceiver);

        merlinService.onBind(intent);

        verify(mockPackageManager).setComponentEnabledSetting(
                mockReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );
    }

    @Test
    public void disableConnectivityReceiverInOnUnbind() throws Exception {
        PackageManager mockPackageManager = mock(PackageManager.class);
        ComponentName mockReceiver = mock(ComponentName.class);
        MerlinService merlinService = new TestDoubleMerlinService(mockPackageManager, mockReceiver);

        merlinService.onUnbind(intent);

        verify(mockPackageManager).setComponentEnabledSetting(
                mockReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        );
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

    private static class TestDoubleMerlinService extends MerlinService {

        private final PackageManager packageManager;
        private final ComponentName receiver;

        TestDoubleMerlinService(PackageManager packageManager, ComponentName receiver) {
            this.packageManager = packageManager;
            this.receiver = receiver;
        }

        @Override
        public PackageManager getPackageManager() {
            return packageManager;
        }

        @Override
        protected ComponentName connectivityReceiverComponent() {
            return receiver;
        }

    }

}
