package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.novoda.merlin.service.AndroidVersion;
import com.novoda.merlin.service.ConnectivityChangeEventExtractor;
import com.novoda.merlin.service.MerlinService;

public class ConnectivityChangesRegister {

    private final Context context;
    private final ConnectivityManager connectivityManager;
    private final AndroidVersion androidVersion;
    private final ConnectivityChangeEventExtractor connectivityChangeEventExtractor;

    private ConnectivityReceiver connectivityReceiver;
    private ConnectivityCallbacks connectivityCallbacks;

    public ConnectivityChangesRegister(Context context,
                                       ConnectivityManager connectivityManager,
                                       AndroidVersion androidVersion,
                                       ConnectivityChangeEventExtractor connectivityChangeEventExtractor) {
        this.context = context;
        this.connectivityManager = connectivityManager;
        this.androidVersion = androidVersion;
        this.connectivityChangeEventExtractor = connectivityChangeEventExtractor;
    }

    public void register(MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier) {
        if (androidVersion.isLollipopOrHigher()) {
            registerNetworkCallbacks(connectivityChangesNotifier);
        } else {
            registerBroadcastReceiver();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void registerNetworkCallbacks(MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier) {
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(builder.build(), connectivityCallbacks(connectivityChangesNotifier));
    }

    private ConnectivityCallbacks connectivityCallbacks(MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier) {
        if (connectivityCallbacks == null) {
            connectivityCallbacks = new ConnectivityCallbacks(connectivityChangesNotifier, connectivityChangeEventExtractor);
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
