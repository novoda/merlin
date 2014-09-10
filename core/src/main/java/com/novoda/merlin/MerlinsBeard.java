package com.novoda.merlin;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MerlinsBeard {

    private ConnectivityManager connectivityManager;

    public static MerlinsBeard from(ContextWrapper content) {
        Context context = content.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return new MerlinsBeard(connectivityManager);
    }

    private MerlinsBeard(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = getNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    private NetworkInfo getNetworkInfo() {
        return connectivityManager.getActiveNetworkInfo();
    }
}
