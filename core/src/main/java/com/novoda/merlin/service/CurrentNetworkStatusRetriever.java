package com.novoda.merlin.service;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;

class CurrentNetworkStatusRetriever {

    private final MerlinsBeard merlinsBeard;
    private final HostPinger hostPinger;

    public CurrentNetworkStatusRetriever(MerlinsBeard merlinsBeard, HostPinger hostPinger) {
        this.merlinsBeard = merlinsBeard;
        this.hostPinger = hostPinger;
    }

    public void fetchWithPing() {
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
