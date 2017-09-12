package com.novoda.merlin.receiver;

import com.novoda.merlin.NetworkStatus;

public class ConnectivityChangeEvent {

    private static final boolean WITHOUT_CONNECTION = false;
    private static final String WITHOUT_REASON = "";
    private static final String WITHOUT_INFO = "";

    private final boolean isConnected;
    private final String info;
    private final String reason;

    public static ConnectivityChangeEvent createWithoutConnection() {
        return new ConnectivityChangeEvent(WITHOUT_CONNECTION, WITHOUT_INFO, WITHOUT_REASON);
    }

    public static ConnectivityChangeEvent createWithNetworkInfoChangeEvent(boolean isConnected, String info, String reason) {
        return new ConnectivityChangeEvent(isConnected, info, reason);
    }

    private ConnectivityChangeEvent(boolean isConnected, String info, String reason) {
        this.isConnected = isConnected;
        this.info = info;
        this.reason = reason;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String info() {
        return info;
    }

    public String reason() {
        return reason;
    }

    public NetworkStatus asNetworkStatus() {
        return isConnected() ? NetworkStatus.newAvailableInstance() : NetworkStatus.newUnavailableInstance();
    }

    @Override
    public String toString() {
        return "ConnectivityChangeEvent{"
                + "isConnected=" + isConnected
                + ", info='" + info + '\''
                + ", reason='" + reason + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConnectivityChangeEvent that = (ConnectivityChangeEvent) o;

        if (isConnected != that.isConnected) {
            return false;
        }
        if (info != null ? !info.equals(that.info) : that.info != null) {
            return false;
        }
        return reason != null ? reason.equals(that.reason) : that.reason == null;

    }

    @Override
    public int hashCode() {
        int result = (isConnected ? 1 : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }

}
