package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.novoda.merlin.service.MerlinService;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MerlinNetworkCallbacks extends ConnectivityManager.NetworkCallback {

    private final ConnectivityManager connectivityManager;
    private final MerlinService merlinService;

    public MerlinNetworkCallbacks(ConnectivityManager connectivityManager, MerlinService merlinService) {
        this.connectivityManager = connectivityManager;
        this.merlinService = merlinService;
    }

    @Override
    public void onAvailable(Network network) {
        notifyMerlinService(network);
    }

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        notifyMerlinService(network);
    }

    @Override
    public void onLost(Network network) {
        notifyMerlinService(network);
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        notifyMerlinService(network);
    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        notifyMerlinService(network);
    }

    private void notifyMerlinService(Network network) {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
        boolean connected = networkInfo.isConnected();
        String reason = networkInfo.getReason();
        String extraInfo = networkInfo.getExtraInfo();

        merlinService.onConnectivityChanged(new ConnectivityChangeEvent(connected, extraInfo, reason));
    }

}
