package com.novoda.merlin;

import android.content.Intent;
import android.net.ConnectivityManager;

class ConnectivityChangeEventCreator {

    ConnectivityChangeEvent createFrom(Intent intent, MerlinsBeard merlinsBeard) {
        boolean isConnected = extractIsConnectedFrom(intent, merlinsBeard);
        String info = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
        String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
        return ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(isConnected, info, reason);
    }

    private boolean extractIsConnectedFrom(Intent intent, MerlinsBeard merlinsBeard) {
        if (intent.hasExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
            return !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        } else {
            return merlinsBeard.isConnected();
        }
    }

}
