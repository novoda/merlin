package com.novoda.merlin;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ConnectivityChangeEventExtractor {

    private final ConnectivityManager connectivityManager;

    ConnectivityChangeEventExtractor(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    ConnectivityChangeEvent extractFrom(Network network) {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);

        if (null != networkInfo) {
            boolean connected = networkInfo.isConnected();
            String reason = networkInfo.getReason();
            String extraInfo = networkInfo.getExtraInfo();

            return ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(connected, extraInfo, reason);
        } else {
            return ConnectivityChangeEvent.createWithoutConnection();
        }
    }

}
