package com.novoda.merlin.demo.connectivity.display;

import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.novoda.merlin.demo.R;

class MerlinSnackbar {

    private final Snackbar snackbar;

    static MerlinSnackbar withDuration(Resources resources, View attachTo, @StringRes int message) {
        int duration = resources.getInteger(R.integer.snackbar_duration);
        Snackbar snackbar = Snackbar.make(attachTo, message, duration);
        return new MerlinSnackbar(snackbar);
    }

    private MerlinSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    MerlinSnackbar setAction(@StringRes int textResourceId, View.OnClickListener listener) {
        snackbar.setAction(textResourceId, listener);
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
