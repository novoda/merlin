package com.novoda.merlin.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.RxCallbacksManager;
import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
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
    @Mock
    private RxCallbacksManager rxCallbacksManager;

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
                    rxCallbacksManager.onConnect();
                } else {
                    disconnectListener.onDisconnect();
                    rxCallbacksManager.onDisconnect();
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
        verify(rxCallbacksManager).onConnect();
    }

    @Test
    public void callMerlinNetworkListenerOnDisconnectedWhenAConnectedConnectivityEventIsTriggered() throws Exception {
        boolean isConnected = false;
        ConnectivityChangeEvent connectivityChangeEvent = createConnectivityChangeEvent(isConnected);

        merlinService.onConnectivityChanged(connectivityChangeEvent);

        verify(disconnectListener).onDisconnect();
        verify(rxCallbacksManager).onDisconnect();
    }

    @Test
    public void useDefaultNetworkStatusRetrieverWhenPingingDefaultEndpoint() {
        ContextWrapper contextWrapper = stubbedContextWrapper();

        NetworkStatusRetriever mockRetriever = mock(NetworkStatusRetriever.class);
        HostPinger mockDefaultPinger = mock(HostPinger.class);
        HostPinger mockCustomPinger = mock(HostPinger.class);

        MerlinService merlinService = buildStubbedMerlinService(contextWrapper, mockRetriever, mockDefaultPinger, mockCustomPinger);

        merlinService.onCreate();
        merlinService.onConnectivityChanged(networkAvailableEvent());
        verify(mockRetriever).fetchWithPing(mockDefaultPinger);
    }

    @Test
    public void useCustomNetworkStatusRetrieverWhenPingingCustomEndpoint() {
        ContextWrapper contextWrapper = stubbedContextWrapper();

        NetworkStatusRetriever mockRetriever = mock(NetworkStatusRetriever.class);
        HostPinger mockDefaultPinger = mock(HostPinger.class);
        HostPinger mockCustomPinger = mock(HostPinger.class);

        MerlinService merlinService = buildStubbedMerlinService(contextWrapper, mockRetriever, mockDefaultPinger, mockCustomPinger);

        merlinService.onCreate();
        merlinService.setHostname("some new host name", mock(ResponseCodeValidator.class));
        merlinService.onConnectivityChanged(networkAvailableEvent());
        verify(mockRetriever).fetchWithPing(mockCustomPinger);
    }

    @NonNull
    private ContextWrapper stubbedContextWrapper() {
        Context context = mock(Context.class);
        ContextWrapper contextWrapper = mock(ContextWrapper.class);
        ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        when(contextWrapper.getApplicationContext()).thenReturn(context);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        return contextWrapper;
    }

    @NonNull
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

    @NonNull
    private ConnectivityChangeEvent networkAvailableEvent() {
        ConnectivityChangeEvent connectivityChangeEvent = mock(ConnectivityChangeEvent.class);
        when(connectivityChangeEvent.asNetworkStatus()).thenReturn(NetworkStatus.newAvailableInstance());
        return connectivityChangeEvent;
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

    private static class TestDoubleMerlinServiceWithStubbedBuilders extends MerlinService {

        private final ContextWrapper context;
        private final NetworkStatusRetriever networkStatusRetriever;
        private final HostPinger defaultHostPinger;
        private final HostPinger customHostPinger;

        TestDoubleMerlinServiceWithStubbedBuilders(
                ContextWrapper context,
                NetworkStatusRetriever networkStatusRetriever,
                HostPinger defaultHostPinger,
                HostPinger customHostPinger
        ) {
            this.context = context;
            this.networkStatusRetriever = networkStatusRetriever;
            this.defaultHostPinger = defaultHostPinger;
            this.customHostPinger = customHostPinger;
        }

        @Override
        public ContextWrapper getApplicationContext() {
            return context;
        }

        @Override
        protected NetworkStatusRetriever buildCurrentNetworkStatusRetriever() {
            return networkStatusRetriever;
        }

        @Override
        protected HostPinger buildDefaultHostPinger() {
            return defaultHostPinger;
        }

        @Override
        protected HostPinger buildHostPinger(String hostName, ResponseCodeValidator validator) {
            return customHostPinger;
        }
    }
}
