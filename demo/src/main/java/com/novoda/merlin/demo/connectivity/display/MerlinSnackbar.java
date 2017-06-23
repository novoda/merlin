package com.novoda.merlin.demo.connectivity.display;

import android.content.res.Resources;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

class MerlinSnackbar {

    private static final String EMPTY_MESSAGE = "";
    private final Snackbar snackbar;

    @NonNull
    static MerlinSnackbar withDuration(Resources resources, View attachTo, @IntegerRes int durationResource) {
        int duration = resources.getInteger(durationResource);
        Snackbar snackbar = Snackbar.make(attachTo, EMPTY_MESSAGE, duration);
        return new MerlinSnackbar(snackbar);
    }

    private MerlinSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    MerlinSnackbar withTheme(Themer themer) {
        themer.applyTheme(snackbar.getContext().getResources(), snackbar);
        return this;
    }

    MerlinSnackbar withText(String message) {
        snackbar.setText(message);
        return this;
    }

    MerlinSnackbar withText(@StringRes int messageResource) {
        snackbar.setText(messageResource);
        return this;
    }

    MerlinSnackbar show() {
        if (!snackbar.isShown()) {
            snackbar.show();
        }
        return this;
    }

    void dismiss() {
        snackbar.dismiss();
    }
}
