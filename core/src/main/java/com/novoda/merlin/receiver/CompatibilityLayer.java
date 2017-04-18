package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.novoda.merlin.service.AndroidVersion;

public class CompatibilityLayer {

    private final Context context;
    private final ConnectivityManager connectivityManager;
    private final AndroidVersion androidVersion;

    private ConnectivityReceiver connectivityReceiver;
    private MerlinNetworkCallbacks merlinNetworkCallbacks;

    public CompatibilityLayer(Context context, ConnectivityManager connectivityManager, AndroidVersion androidVersion) {
        this.context = context;
        this.connectivityManager = connectivityManager;
        this.androidVersion = androidVersion;
    }

    public void bind() {
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
            merlinNetworkCallbacks = new MerlinNetworkCallbacks();
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

    public void unbind() {
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
