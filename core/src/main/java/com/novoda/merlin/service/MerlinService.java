package com.novoda.merlin.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.VisibleForTesting;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.RxCallbacksManager;
import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.receiver.ConnectivityReceiver;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

public class MerlinService extends Service implements HostPinger.PingerCallback {

    public static boolean USE_COMPONENT_ENABLED_SETTING = true;

    private final IBinder binder;
    private CurrentNetworkStatusRetriever currentNetworkStatusRetriever;
    private HostPinger hostPinger;

    private ConnectListener connectListener;
    private DisconnectListener disconnectListener;

    private RxCallbacksManager rxCallbacksManager;

    private NetworkStatus networkStatus;

    public MerlinService() {
        binder = new LocalBinder();
        hostPinger = HostPinger.withDefaultEndpointValidation(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        currentNetworkStatusRetriever = new CurrentNetworkStatusRetriever(MerlinsBeard.from(this.getApplicationContext()), hostPinger);
    }

    public class LocalBinder extends Binder {
        public MerlinService getService() {
            return MerlinService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        enableConnectivityReceiver();
        return binder;
    }

    private void enableConnectivityReceiver() {
        setReceiverState(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disableConnectivityReceiver();
        return super.onUnbind(intent);
    }

    private void disableConnectivityReceiver() {
        setReceiverState(PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    private void setReceiverState(int receiverState) {
        if (USE_COMPONENT_ENABLED_SETTING) {
            getPackageManager().setComponentEnabledSetting(connectivityReceiverComponent(), receiverState, PackageManager.DONT_KILL_APP);
        }
    }

    @VisibleForTesting
    protected ComponentName connectivityReceiverComponent() {
        return new ComponentName(this, ConnectivityReceiver.class);
    }

    public void setHostname(String hostname, ResponseCodeValidator validator) {
        hostPinger = HostPinger.withCustomEndpointAndValidation(this, hostname, validator);
        currentNetworkStatusRetriever = new CurrentNetworkStatusRetriever(MerlinsBeard.from(this.getApplicationContext()), hostPinger);
    }

    public void setBindStatusListener(BindListener bindListener) {
        callbackCurrentStatus(bindListener);
    }

    public void setRxCallbacksManager(RxCallbacksManager rxCallbacksManager) {
        this.rxCallbacksManager = rxCallbacksManager;
    }

    private void callbackCurrentStatus(BindListener bindListener) {
        if (bindListener != null) {
            if (networkStatus == null) {
                bindListener.onMerlinBind(currentNetworkStatusRetriever.get());
                return;
            }
            bindListener.onMerlinBind(networkStatus);
        }
    }

    public void setConnectListener(ConnectListener connectListener) {
        this.connectListener = connectListener;
    }

    public void setDisconnectListener(DisconnectListener disconnectListener) {
        this.disconnectListener = disconnectListener;
    }

    public void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent) {
        if (!connectivityChangeEvent.asNetworkStatus().equals(networkStatus)) {
            getCurrentNetworkStatus();
        }
        networkStatus = connectivityChangeEvent.asNetworkStatus();
    }

    private void getCurrentNetworkStatus() {
        currentNetworkStatusRetriever.fetchWithPing();
    }

    @Override
    public void onSuccess() {
        networkStatus = NetworkStatus.newAvailableInstance();
        if (connectListener != null) {
            connectListener.onConnect();
        }
        if (rxCallbacksManager != null) {
            rxCallbacksManager.onConnect();
        }
    }

    @Override
    public void onFailure() {
        networkStatus = NetworkStatus.newUnavailableInstance();
        if (disconnectListener != null) {
            disconnectListener.onDisconnect();
        }
        if (rxCallbacksManager != null) {
            rxCallbacksManager.onDisconnect();
        }
    }

}
