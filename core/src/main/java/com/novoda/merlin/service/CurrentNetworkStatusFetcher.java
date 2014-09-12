package com.novoda.merlin.service;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;

class CurrentNetworkStatusFetcher {

    private final MerlinsBeard merlinsBeard;
    private final HostPinger hostPinger;

    public CurrentNetworkStatusFetcher(MerlinsBeard merlinsBeard, HostPinger hostPinger) {
        this.merlinsBeard = merlinsBeard;
        this.hostPinger = hostPinger;
    }

    public void fetch() {
        if (merlinsBeard.isConnected()) {
            hostPinger.ping();
        } else {
            hostPinger.noNetworkToPing();
        }
    }

    public NetworkStatus getWithoutPing() {
        if (merlinsBeard.isConnected()) {
            return NetworkStatus.newAvailableInstance();
        } else {
            return NetworkStatus.newUnavailableInstance();
        }
    }

}
