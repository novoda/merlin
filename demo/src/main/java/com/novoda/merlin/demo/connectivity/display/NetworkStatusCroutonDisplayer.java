package com.novoda.merlin.demo.connectivity.display;

import android.app.Activity;

public class NetworkStatusCroutonDisplayer implements NetworkStatusDisplayer {

    private final NovodaCrouton novodaCrouton;

    public NetworkStatusCroutonDisplayer(Activity activity) {
        this.novodaCrouton = new NovodaCrouton(activity);
    }

    @Override
    public void displayConnected() {
        novodaCrouton.show(CroutonStyles.CONNECTED);
    }

    @Override
    public void displayDisconnected() {
        if (!novodaCrouton.isShown()) {
            novodaCrouton.show(CroutonStyles.DISCONNECTED);
        }
    }

    @Override
    public void reset() {
        novodaCrouton.close();
    }

}
