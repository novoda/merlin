package com.novoda.merlin.demo.connectivity.display;

import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.view.View;

import com.novoda.merlin.demo.R;

public class NetworkStatusDisplayer {

    private final Resources resources;

    private MerlinSnackbar snackbar;

    public NetworkStatusDisplayer(Resources resources) {
        this.resources = resources;
    }

    public void displayPositiveMessage(@StringRes int messageResource, View attachTo) {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo, R.integer.snackbar_duration);
        snackbar.withText(messageResource)
                .withTheme(new PositiveThemer())
                .show();
    }

    public void displayNegativeMessage(@StringRes int messageResource, View attachTo) {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo, R.integer.snackbar_duration);
        snackbar.withText(messageResource)
                .withTheme(new NegativeThemer())
                .show();
    }

    public void displayNetworkSubtype(String subtype, View attachTo) {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo, R.integer.snackbar_duration);

        if (subtypeAbsent(subtype)) {
            snackbar.withText(R.string.subtype_not_available)
                    .withTheme(new NegativeThemer())
                    .show();
        } else {
            snackbar.withText(resources.getString(R.string.subtype_value, subtype))
                    .withTheme(new PositiveThemer())
                    .show();
        }
    }

    private boolean subtypeAbsent(String subtype) {
        return subtype == null || subtype.isEmpty();
    }

    public void reset() {
        if (snackbar == null) {
            return;
        }
        snackbar.dismiss();
        snackbar = null;
    }

}
