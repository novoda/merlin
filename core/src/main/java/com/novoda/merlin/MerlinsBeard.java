package com.novoda.merlin;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.WorkerThread;

/**
 * This class provides a mechanism for retrieving the current
 * state of a network connection given an application context.
 */
public class MerlinsBeard {

    private static final boolean IS_NOT_CONNECTED_TO_NETWORK_TYPE = false;

    private final ConnectivityManager connectivityManager;
    private final AndroidVersion androidVersion;
    private final EndpointPinger captivePortalPinger;
    private final Ping captivePortalPing;

    /**
     * Use this method to create a MerlinsBeard object, this is how you can retrieve the current network state.
     *
     * @param context pass any context application or activity.
     * @return MerlinsBeard.
     */
    public static MerlinsBeard from(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        AndroidVersion androidVersion = new AndroidVersion();
        ResponseCodeValidator validator = new ResponseCodeValidator.CaptivePortalEndpointResponseCodeValidator();
        EndpointPinger captivePortalpinger = EndpointPinger.withCustomEndpointAndValidation(Endpoint.captivePortalEndpoint(), validator);
        Ping captivePortalPing = new Ping(Endpoint.captivePortalEndpoint(), new EndpointPinger.ResponseCodeFetcher(), validator);

        return new MerlinsBeard(connectivityManager, androidVersion, captivePortalpinger, captivePortalPing);
    }

    MerlinsBeard(ConnectivityManager connectivityManager, AndroidVersion androidVersion, EndpointPinger captivePortalPinger, Ping CaptivePortalPing) {
        this.connectivityManager = connectivityManager;
        this.androidVersion = androidVersion;
        this.captivePortalPinger = captivePortalPinger;
        this.captivePortalPing = CaptivePortalPing;
    }

    /**
     * Provides a boolean representing whether a network connection has been established.
     * NOTE: Therefore available does not necessarily mean that an internet connection
     * is available.
     *
     * @return boolean true if a network connection is available.
     */
    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = networkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private NetworkInfo networkInfo() {
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * Provides a boolean representing whether a mobile network connection has been established and is active.
     * <p>
     * NOTE: Therefore available does not necessarily mean that an internet connection
     * is available. Also, there can be only one network connection at a time, so this would return false if
     * the active connection is the Wi-Fi one, even if there is a (inactive) mobile network connection established.
     * </p>
     *
     * @return boolean true if a mobile network connection is available.
     */
    public boolean isConnectedToMobileNetwork() {
        return isConnectedTo(ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Provides a boolean representing whether a Wi-Fi network connection has been established.
     * <p>
     * NOTE: Therefore available does not necessarily mean that an internet connection
     * is available.
     * </p>
     *
     * @return boolean true if a Wi-Fi network connection is available.
     */
    public boolean isConnectedToWifi() {
        return isConnectedTo(ConnectivityManager.TYPE_WIFI);
    }

    private boolean isConnectedTo(int networkType) {
        if (androidVersion.isLollipopOrHigher()) {
            return connectedToNetworkTypeForLollipop(networkType);
        }

        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);
        return networkInfo != null && networkInfo.isConnected();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean connectedToNetworkTypeForLollipop(int networkType) {
        Network[] networks = connectivityManager.getAllNetworks();

        for (Network network : networks) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);

            if (networkInfo != null && networkInfo.getType() == networkType) {
                return networkInfo.isConnected();
            }

        }

        return IS_NOT_CONNECTED_TO_NETWORK_TYPE;
    }

    /**
     * Provides a human-readable String describing the network subtype (e.g. UMTS, LTE) when connected to a mobile network.
     *
     * @return network subtype name, or empty string if not connected to a mobile network.
     */
    public String getMobileNetworkSubtypeName() {
        NetworkInfo networkInfo = networkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return "";
        }
        return networkInfo.getSubtypeName();
    }

    /**
     * Detects if client is behind a captive portal.
     *
     * @param callback to call with boolean result representing if client is behind a captive portal.
     */
    public void isCaptivePortal(final CaptivePortalDetectionCallback callback) {
        captivePortalPinger.ping(new EndpointPinger.PingerCallback() {
            @Override
            public void onSuccess() {
                callback.onResult(false);
            }

            @Override
            public void onFailure() {
                callback.onResult(true);
            }
        });
    }

    /**
     * Detects if client is behind a captive portal - synchronously.
     *
     * @return Boolean result representing if client is behind a captive portal.
     */
    @WorkerThread
    public boolean isCaptivePortal() {
        return !captivePortalPing.doSynchronousPing();
    }

    public interface CaptivePortalDetectionCallback {
        void onResult(boolean isCaptivePortal);
    }

}
