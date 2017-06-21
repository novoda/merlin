package com.novoda.merlin.demo.connectivity.display;

import android.content.res.Resources;
import android.support.annotation.StringRes;
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

    public void displayPositiveMessage(@StringRes int messageResource) {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo);
        snackbar.withText(messageResource)
                .withTheme(new PositiveThemer())
                .show();
    }

    public void displayNegativeMessage(@StringRes int messageResource) {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo);
        snackbar.withText(messageResource)
                .withTheme(new NegativeThemer())
                .show();
    }

    public void displayNetworkSubtype(String subtype) {
        snackbar = MerlinSnackbar.withDuration(resources, attachTo);

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
        snackbar.dismiss();
    }

}
