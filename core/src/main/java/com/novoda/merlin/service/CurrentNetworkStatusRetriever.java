package com.novoda.merlin.service;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;

class CurrentNetworkStatusRetriever {

    private final MerlinsBeard merlinsBeard;

    public CurrentNetworkStatusRetriever(MerlinsBeard merlinsBeard) {
        this.merlinsBeard = merlinsBeard;
    }

    public void fetchWithPing(HostPinger hostPinger) {
        if (merlinsBeard.isConnected()) {
            hostPinger.ping();
        } else {
            hostPinger.noNetworkToPing();
        }
    }

    public NetworkStatus get() {
        if (merlinsBeard.isConnected()) {
            return NetworkStatus.newAvailableInstance();
        } else {
            return NetworkStatus.newUnavailableInstance();
        }
    }

}
