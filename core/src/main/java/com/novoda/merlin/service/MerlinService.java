package com.novoda.merlin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.receiver.ConnectivityChangesRegister;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

public class MerlinService extends Service implements HostPinger.PingerCallback {

    private static boolean isBound;

    private final IBinder binder;

    private ConnectivityChangesRegister connectivityChangesRegister;
    private NetworkStatusRetriever networkStatusRetriever;
    private DisconnectListener disconnectListener;
    private ConnectListener connectListener;
    private BindListener bindListener;
    private HostPinger hostPinger;

    private NetworkStatus networkStatus;

    public MerlinService() {
        binder = new LocalBinder();
    }

    MerlinService(MerlinService.LocalBinder binder) {
        this.binder = binder;
    }

    public static boolean isBound() {
        return isBound;
    }

    @Override
    public IBinder onBind(Intent intent) {
        isBound = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBound = false;
        connectivityChangesRegister.unregister();
        return super.onUnbind(intent);
    }

    private void start() {
        notifyOfInitialNetworkStatus();
        connectivityChangesRegister.register(connectivityChangesListener);
    }

    private void notifyOfInitialNetworkStatus() {
        if (networkStatus == null) {
            bindListener.onMerlinBind(networkStatusRetriever.retrieveNetworkStatus());
            return;
        }
        bindListener.onMerlinBind(networkStatus);
    }

    private final ConnectivityChangesListener connectivityChangesListener = new ConnectivityChangesListener() {
        @Override
        public void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent) {
            if (!connectivityChangeEvent.asNetworkStatus().equals(networkStatus)) {
                networkStatusRetriever.fetchWithPing(hostPinger, MerlinService.this);
            }
            networkStatus = connectivityChangeEvent.asNetworkStatus();
        }
    };

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

    public interface ConnectivityChangesListener {
        void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent);
    }

    public class LocalBinder extends Binder {

        public ConnectivityChangesListener getConnectivityChangedListener() {
            return MerlinService.this.connectivityChangesListener;
        }

        void setConnectivityChangesRegister(ConnectivityChangesRegister connectivityChangesRegister) {
            MerlinService.this.connectivityChangesRegister = connectivityChangesRegister;
        }

        void setNetworkStatusRetriever(NetworkStatusRetriever networkStatusRetriever) {
            MerlinService.this.networkStatusRetriever = networkStatusRetriever;
        }

        void setDisconnectListener(DisconnectListener disconnectListener) {
            MerlinService.this.disconnectListener = disconnectListener;
        }

        void setConnectListener(ConnectListener connectListener) {
            MerlinService.this.connectListener = connectListener;
        }

        void setBindListener(BindListener bindListener) {
            MerlinService.this.bindListener = bindListener;
        }

        void setHostPinger(HostPinger hostPinger) {
            MerlinService.this.hostPinger = hostPinger;
        }

        void onBindComplete() {
            MerlinService.this.start();
        }
    }

}
