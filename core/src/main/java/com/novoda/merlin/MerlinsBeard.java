package com.novoda.merlin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class provides a mechanism for retrieving the current
 * state of a network connection given an application context.
 */
public class MerlinsBeard {

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

    /**
     * Provides a boolean representing whether a wifi network connection has been established.
     *
     * NOTE: Therefore available does not necessarily mean that an internet connection
     * is available.
     *
     * @return boolean true if a wifi network connection is available.
     */
    public boolean isConnectedToWifi() {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }

}
