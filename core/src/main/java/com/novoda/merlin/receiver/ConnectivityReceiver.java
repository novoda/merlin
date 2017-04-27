package com.novoda.merlin.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.VisibleForTesting;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

public class ConnectivityReceiver extends BroadcastReceiver {

    // Lint thinks we're not calling getAction, but we actually do
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && connectivityAction(intent)) {
            boolean isConnected = getIsConnected(context, intent);
            String info = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            notifyMerlinService(context, ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(isConnected, info, reason));
        }
    }

    private boolean connectivityAction(Intent intent) {
        return ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction());
    }

    private boolean getIsConnected(Context context, Intent intent) {
        if (intent.hasExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
            return !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        } else {
            return getMerlinsBeard(context).isConnected();
        }
    }

    private void notifyMerlinService(Context context, ConnectivityChangeEvent connectivityChangedEvent) {
        MerlinService merlinService = getMerlinService(context);
        if (isAvailable(merlinService)) {
            merlinService.onConnectivityChanged(connectivityChangedEvent);
        }
    }

    private static boolean isAvailable(Object object) {
        return object != null;
    }

    @VisibleForTesting
    protected MerlinService getMerlinService(Context context) {
        IBinder binder = peekService(context, new Intent(context, MerlinService.class));
        if (isAvailable(binder)) {
            return ((MerlinService.LocalBinder) binder).getService();
        }
        return null;
    }

    @VisibleForTesting
    protected MerlinsBeard getMerlinsBeard(Context context) {
        return MerlinsBeard.from(context);
    }

}
