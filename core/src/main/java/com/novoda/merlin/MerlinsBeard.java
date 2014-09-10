package com.novoda.merlin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MerlinsBeard {

    private final Context context;

    public static MerlinsBeard from(Context context) {
        return new MerlinsBeard(context);
    }

    private MerlinsBeard(Context context) {
        this.context = context;
    }

    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = getNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }
}
