package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.novoda.merlin.service.AndroidVersion;
import com.novoda.merlin.service.MerlinService;

public class ConnectivityChangesRegistrar {

    private final Context context;
    private final ConnectivityManager connectivityManager;
    private final AndroidVersion androidVersion;
    private final MerlinService merlinService;

    private ConnectivityReceiver connectivityReceiver;
    private MerlinNetworkCallbacks merlinNetworkCallbacks;

    public ConnectivityChangesRegistrar(Context context,
                                        ConnectivityManager connectivityManager,
                                        AndroidVersion androidVersion,
                                        MerlinService merlinService) {
        this.context = context;
        this.connectivityManager = connectivityManager;
        this.androidVersion = androidVersion;
        this.merlinService = merlinService;
    }

    public void register() {
        if (androidVersion.isLollipopOrHigher()) {
            registerNetworkCallbacks();
        } else {
            registerBroadcastReceiver();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void registerNetworkCallbacks() {
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(builder.build(), getMerlinNetworkCallbacks());
    }

    private MerlinNetworkCallbacks getMerlinNetworkCallbacks() {
        if (merlinNetworkCallbacks == null) {
            merlinNetworkCallbacks = new MerlinNetworkCallbacks(connectivityManager, merlinService);
        }
        return merlinNetworkCallbacks;
    }

    private void registerBroadcastReceiver() {
        context.registerReceiver(getConnectivityReceiver(), getConnectivityActionIntentFilter());
    }

    private ConnectivityReceiver getConnectivityReceiver() {
        if (connectivityReceiver == null) {
            connectivityReceiver = new ConnectivityReceiver();
        }
        return connectivityReceiver;
    }

    private IntentFilter getConnectivityActionIntentFilter() {
        return new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public void unregister() {
        if (androidVersion.isLollipopOrHigher()) {
            unregisterNetworkCallbacks();
        } else {
            unregisterBroadcastReceiver();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void unregisterNetworkCallbacks() {
        connectivityManager.unregisterNetworkCallback(getMerlinNetworkCallbacks());
    }

    private void unregisterBroadcastReceiver() {
        context.unregisterReceiver(getConnectivityReceiver());
    }

}
