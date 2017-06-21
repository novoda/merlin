package com.novoda.merlin.demo.connectivity.display;

import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.novoda.merlin.demo.R;

class MerlinSnackbar {

    private static final String EMPTY_MESSAGE = "";
    private final Snackbar snackbar;

    static MerlinSnackbar withDuration(Resources resources, View attachTo) {
        int duration = resources.getInteger(R.integer.snackbar_duration);
        Snackbar snackbar = Snackbar.make(attachTo, EMPTY_MESSAGE, duration);
        return new MerlinSnackbar(snackbar);
    }

    private MerlinSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    MerlinSnackbar withTheme(Themer themer) {
        themer.theme(snackbar.getContext().getResources(), snackbar);
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

    void show() {
        if (!snackbar.isShown()) {
            snackbar.show();
        }
    }

    void dismiss() {
        snackbar.dismiss();
    }
}
