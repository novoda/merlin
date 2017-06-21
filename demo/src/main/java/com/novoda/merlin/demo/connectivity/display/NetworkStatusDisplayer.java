package com.novoda.merlin.demo.connectivity.display;

import android.content.res.Resources;
import android.view.View;

import com.novoda.merlin.demo.R;

public class NetworkStatusDisplayer {

    private final Resources resources;
    private final View attachTo;

    private MerlinSnackbar snackbar;

    public NetworkStatusDisplayer(Resources resources, View attachTo) {
        this.resources = resources;
        this.attachTo = attachTo;
    }

    public void displayConnected() {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo, R.string.network_connected);
        snackbar.withTheme(new PositiveThemer())
                .show();
    }

    public void displayDisconnected() {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo, R.string.network_disconnected);
        snackbar.withTheme(new NegativeThemer())
                .show();
    }

    public void displayNetworkSubtype(String networkSubtype) {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo, networkSubtype);
    }

    public void reset() {
        snackbar.dismiss();
    }

}
