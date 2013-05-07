package com.merlin.receiver.event;

import android.content.Intent;
import android.net.ConnectivityManager;

public class ConnectionEventPackager {

    public static ConnectivityChangeEvent from(Intent intent) {
        String info = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
        String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
        return new ConnectivityChangeEvent(isConnected(intent), info, reason);
    }

    private static boolean isConnected(Intent intent) {
        return !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
    }

}
