package com.novoda.merlin.demo.connectivity.display;

import android.content.res.Resources;
import android.view.View;

import com.novoda.merlin.demo.R;

public class NetworkStatusSnackbarDisplayer implements NetworkStatusDisplayer {

    private final Resources resources;
    private final View attachTo;

    private MerlinSnackbar snackbar;

    public NetworkStatusSnackbarDisplayer(Resources resources, View attachTo) {
        this.resources = resources;
        this.attachTo = attachTo;
    }

    @Override
    public void displayConnected() {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo, R.string.network_connected);
        snackbar.show();
    }

    @Override
    public void displayDisconnected() {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo, R.string.network_disconnected);
        snackbar.show();
    }

    @Override
    public void reset() {
        snackbar.dismiss();
    }

}
