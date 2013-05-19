package com.novoda.merlin.demo.connectivity.display;

import android.app.Activity;
import android.view.View;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;
import de.keyboardsurfer.android.widget.crouton.Style;

public class NovodaCrouton {

    private final Activity activity;

    private Crouton currentCrouton;

    public NovodaCrouton(Activity activity) {
        this.activity = activity;
    }

    public boolean isShown() {
        return currentCrouton != null;
    }

    public void show(CroutonStyles croutonStyles) {
        hideCurrent();
        Crouton crouton = makeCrouton(croutonStyles);
        setClickListener(crouton);
        setCurrent(crouton);
        crouton.show();
    }

    private Crouton makeCrouton(CroutonStyles croutonStyles) {
        Style style = croutonStyles.getStyle(activity);
        return Crouton.makeText(activity, croutonStyles.getStringResId(), style);
    }

    private void hideCurrent() {
        if (isShown()) {
            Crouton.hide(currentCrouton);
        }
    }

    private void setClickListener(final Crouton crouton) {
        crouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCrouton(crouton);
            }
        });
    }

    private void hideCrouton(Crouton crouton) {
        Crouton.hide(crouton);
    }

    private void setCurrent(Crouton crouton) {
        setLifecycle(crouton);
    }

    private void setLifecycle(final Crouton crouton) {
        crouton.setLifecycleCallback(new LifecycleCallback() {
            @Override
            public void onDisplayed() {
                currentCrouton = crouton;
            }

            @Override
            public void onRemoved() {
                currentCrouton = null;
            }
        });
    }

    public void close() {
        Crouton.cancelAllCroutons();
        currentCrouton = null;
    }

}
