package com.novoda.merlin.receiver;

import android.content.Intent;
import android.net.ConnectivityManager;

import com.novoda.merlin.MerlinsBeard;

class ConnectivityChangeEventCreator {

    private final MerlinsBeard merlinsBeard;

    ConnectivityChangeEventCreator(MerlinsBeard merlinsBeard) {
        this.merlinsBeard = merlinsBeard;
    }

    ConnectivityChangeEvent createFrom(Intent intent) {
        boolean isConnected = getConnectivity(intent);
        String info = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
        String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
        return ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(isConnected, info, reason);
    }

    private boolean getConnectivity(Intent intent) {
        if (intent.hasExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
            return !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        } else {
            return merlinsBeard.isConnected();
        }
    }

}
