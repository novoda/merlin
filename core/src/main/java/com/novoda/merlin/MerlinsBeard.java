package com.novoda.merlin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * This class provides a mechanism for retrieving the current
 * state of a network connection given an application context.
 */
public class MerlinsBeard {

    private final ConnectivityManager connectivityManager;
    private final TelephonyManager telephonyManager;

    /**
     * Use this method to create a MerlinsBeard object, this is how you can retrieve the current network state.
     *
     * @param context pass any context application or activity.
     * @return MerlinsBeard.
     */
    public static MerlinsBeard from(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return new MerlinsBeard(connectivityManager, telephonyManager);
    }

    MerlinsBeard(ConnectivityManager connectivityManager, TelephonyManager telephonyManager) {
        this.connectivityManager = connectivityManager;
        this.telephonyManager = telephonyManager;
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
     * Provides a boolean representing whether a Wi-Fi network connection has been established.
     *
     * NOTE: Therefore available does not necessarily mean that an internet connection
     * is available.
     *
     * @return boolean true if a Wi-Fi network connection is available.
     */
    public boolean isConnectedToWifi() {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }


    /**
     * Provides a boolean representing whether a mobile network connection has been established and is active.
     * NOTE: Therefore available does not necessarily mean that an internet connection
     * is available. Also, there can be only one network connection at a time, so this would return false if
     * the active connection is the Wi-Fi one, even if there is a (inactive) mobile network connection established.
     *
     * @return boolean true if a mobile network connection is available.
     */
    public boolean isConnectedToMobileNetwork() {
        int simState = telephonyManager.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }


}
