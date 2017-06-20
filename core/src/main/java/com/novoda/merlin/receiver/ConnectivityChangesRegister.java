package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.novoda.merlin.service.AndroidVersion;
import com.novoda.merlin.service.MerlinService;

public class ConnectivityChangesRegister {

    private final Context context;
    private final ConnectivityManager connectivityManager;
    private final AndroidVersion androidVersion;

    private ConnectivityReceiver connectivityReceiver;
    private ConnectivityCallbacks connectivityCallbacks;

    public ConnectivityChangesRegister(Context context,
                                       ConnectivityManager connectivityManager,
                                       AndroidVersion androidVersion) {
        this.context = context;
        this.connectivityManager = connectivityManager;
        this.androidVersion = androidVersion;
    }

    public void register(MerlinService.ConnectivityChangesListener connectivityChangesListener) {
        if (androidVersion.isLollipopOrHigher()) {
            registerNetworkCallbacks(connectivityChangesListener);
        } else {
            registerBroadcastReceiver();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void registerNetworkCallbacks(MerlinService.ConnectivityChangesListener connectivityChangesListener) {
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(builder.build(), connectivityCallbacks(connectivityChangesListener));
    }

    private ConnectivityCallbacks connectivityCallbacks(MerlinService.ConnectivityChangesListener connectivityChangesListener) {
        if (connectivityCallbacks == null) {
            connectivityCallbacks = new ConnectivityCallbacks(connectivityManager, connectivityChangesListener);
        }
        return connectivityCallbacks;
    }

    private void registerBroadcastReceiver() {
        context.registerReceiver(connectivityReceiver(), connectivityActionIntentFilter());
    }

    private ConnectivityReceiver connectivityReceiver() {
        if (connectivityReceiver == null) {
            connectivityReceiver = new ConnectivityReceiver();
        }
        return connectivityReceiver;
    }

    private IntentFilter connectivityActionIntentFilter() {
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
        connectivityManager.unregisterNetworkCallback(connectivityCallbacks);
    }

    private void unregisterBroadcastReceiver() {
        context.unregisterReceiver(connectivityReceiver);
    }

}
