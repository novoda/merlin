package com.novoda.merlin.service;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

class ConnectivityChangesForwarder {

    private final NetworkStatusRetriever networkStatusRetriever;
    private final DisconnectListener disconnectListener;
    private final ConnectListener connectListener;
    private final BindListener bindListener;
    private final HostPinger hostPinger;

    private NetworkStatus networkStatus;

    ConnectivityChangesForwarder(NetworkStatusRetriever networkStatusRetriever,
                                 DisconnectListener disconnectListener,
                                 ConnectListener connectListener,
                                 BindListener bindListener,
                                 HostPinger hostPinger) {
        this.networkStatusRetriever = networkStatusRetriever;
        this.disconnectListener = disconnectListener;
        this.connectListener = connectListener;
        this.bindListener = bindListener;
        this.hostPinger = hostPinger;
    }

    void notifyOfInitialNetworkStatus() {
        if (networkStatus == null) {
            bindListener.onMerlinBind(networkStatusRetriever.retrieveNetworkStatus());
            return;
        }
        bindListener.onMerlinBind(networkStatus);
    }

    void notifyOf(ConnectivityChangeEvent connectivityChangeEvent) {
        if (!connectivityChangeEvent.asNetworkStatus().equals(networkStatus)) {
            networkStatusRetriever.fetchWithPing(hostPinger, hostPingerCallback);
        }
        networkStatus = connectivityChangeEvent.asNetworkStatus();
    }

    private final HostPinger.PingerCallback hostPingerCallback = new HostPinger.PingerCallback() {
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
    };

}
