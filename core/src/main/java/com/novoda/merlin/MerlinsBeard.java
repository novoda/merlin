package com.novoda.merlin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class provides a mechanism for retrieving the current
 * state of a network connection given an application context.
 */
public class MerlinsBeard {

    public enum NetworkType {
        WIFI(ConnectivityManager.TYPE_WIFI);

        private final int networkType;

        NetworkType(int networkType) {
            this.networkType = networkType;
        }

        int getValue() {
            return networkType;
        }
    }

    private ConnectivityManager connectivityManager;

    /**
     * Use this method to create a MerlinsBeard object, this is how you can retrieve the current network state.
     *
     * @param context pass any context application or activity.
     * @return MerlinsBeard.
     */
    public static MerlinsBeard from(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return new MerlinsBeard(connectivityManager);
    }

    MerlinsBeard(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    /**
     * Provides a boolean representing whether a network connection has been established.
     * NOTE: Therefore available does not necessarily mean that an internet connection
     * is available.
     *
     * @return boolean true if a network connection is available.
     */
    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = getNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private NetworkInfo getNetworkInfo() {
        return connectivityManager.getActiveNetworkInfo();
    }

    public boolean isConnectedTo(NetworkType networkType) {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType.getValue());
        return networkInfo != null && networkInfo.isConnected();
    }

}
