package com.novoda.merlin.receiver;

import com.novoda.merlin.NetworkStatus;

public class ConnectivityChangeEvent {

    private final boolean isConnected;
    private final String info;
    private final String reason;

    public ConnectivityChangeEvent(boolean isConnected, String info, String reason) {
        this.isConnected = isConnected;
        this.info = info;
        this.reason = reason;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getInfo() {
        return info;
    }

    public String getReason() {
        return reason;
    }

    public NetworkStatus asNetworkStatus() {
        return isConnected() ? NetworkStatus.newAvailableInstance() : NetworkStatus.newUnavailableInstance();
    }
}
