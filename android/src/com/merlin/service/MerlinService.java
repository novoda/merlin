package com.merlin.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;

import com.merlin.NetworkStatus;
import com.merlin.receiver.ConnectivityReceiver;
import com.merlin.receiver.event.ConnectivityChangeEvent;
import com.merlin.registerable.connection.ConnectListener;
import com.merlin.registerable.disconnection.DisconnectListener;


public class MerlinService extends Service implements HostPinger.PingerCallback {

    private final IBinder binder;
    private final CurrentNetworkStatusFetcher currentNetworkStatusFetcher;
    private final HostPinger hostPinger;

    private ConnectListener connectListener;
    private DisconnectListener disconnectListener;

    private NetworkStatus networkStatus;

    public MerlinService() {
        binder = new LocalBinder();
        hostPinger = new HostPinger(this);
        currentNetworkStatusFetcher = new CurrentNetworkStatusFetcher(hostPinger);
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
        ComponentName receiver = new ComponentName(this, ConnectivityReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, receiverState, PackageManager.DONT_KILL_APP);
    }

    public void setHostname(String hostname) {
        hostPinger.setHostAddress(hostname);
    }

    public void setBindStatusListener(BindListener bindListener) {
        callbackCurrentStatus(bindListener);
    }

    private void callbackCurrentStatus(BindListener bindListener) {
        if (bindListener != null) {
            if (networkStatus == null) {
                bindListener.onMerlinBind(currentNetworkStatusFetcher.getWithoutPing(this));
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
        currentNetworkStatusFetcher.get(this);
    }

    @Override
    public void onSuccess() {
        networkStatus = NetworkStatus.newAvailableInstance();
        if (connectListener != null) {
            connectListener.onConnect();
        }
    }

    @Override
    public void onFailure() {
        networkStatus = NetworkStatus.newUnavailableInstance();
        if (disconnectListener != null) {
            disconnectListener.onDisconnect();
        }
    }

}
