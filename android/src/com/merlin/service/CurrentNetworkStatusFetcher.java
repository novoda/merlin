package com.merlin.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.merlin.NetworkStatus;

class CurrentNetworkStatusFetcher {

    private final HostPinger hostPinger;

    public CurrentNetworkStatusFetcher(HostPinger hostPinger) {
        this.hostPinger = hostPinger;
    }

    public void get(Context context) {
        NetworkInfo activeNetworkInfo = getNetworkInfo(context);
        if (isConnectable(activeNetworkInfo)) {
            hostPinger.ping();
        } else {
            hostPinger.noNetworkToPing();
        }
    }

    public NetworkStatus getWithoutPing(Context context) {
        NetworkInfo activeNetworkInfo = getNetworkInfo(context);
        if (isConnectable(activeNetworkInfo)) {
            return NetworkStatus.newAvailableInstance();
        } else {
            return NetworkStatus.newUnavailableInstance();
        }
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    private static boolean isConnectable(NetworkInfo activeNetworkInfo) {
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
