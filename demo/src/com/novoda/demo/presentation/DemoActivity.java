package com.novoda.demo.presentation;

import android.os.Bundle;

import com.merlin.Merlin;
import com.merlin.NetworkStatus;
import com.merlin.registerable.connection.Connectable;
import com.merlin.registerable.disconnection.Disconnectable;
import com.merlin.service.BindListener;
import com.novoda.demo.R;
import com.novoda.demo.connectivity.display.NetworkStatusCroutonDisplayer;
import com.novoda.demo.connectivity.display.NetworkStatusDisplayer;
import com.novoda.demo.presentation.base.MerlinActivity;

public class DemoActivity extends MerlinActivity implements Connectable, Disconnectable, BindListener {

    private NetworkStatusDisplayer networkStatusDisplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        networkStatusDisplayer = new NetworkStatusCroutonDisplayer(this);
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindListener(this)
                .withLogging(true)
                .build(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerConnectable(this);
        registerDisconnectable(this);
    }

    @Override
    public void onMerlinBind(NetworkStatus networkStatus) {
        if (!networkStatus.isAvailable()) {
            onDisconnect();
        }
    }

    @Override
    public void onConnect() {
        networkStatusDisplayer.displayConnected();
    }

    @Override
    public void onDisconnect() {
        networkStatusDisplayer.displayDisconnected();
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStatusDisplayer.reset();
    }

}

