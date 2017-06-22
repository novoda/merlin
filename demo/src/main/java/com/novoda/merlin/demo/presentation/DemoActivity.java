package com.novoda.merlin.demo.presentation;

import android.os.Bundle;
import android.view.View;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.demo.R;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer;
import com.novoda.merlin.demo.presentation.base.MerlinActivity;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

public class DemoActivity extends MerlinActivity implements Connectable, Disconnectable, Bindable {

    private NetworkStatusDisplayer networkStatusDisplayer;
    private MerlinsBeard merlinsBeard;
    private View viewToAttachDisplayerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        viewToAttachDisplayerTo = findViewById(R.id.displayerAttachableView);
        merlinsBeard = MerlinsBeard.from(this);
        networkStatusDisplayer = new NetworkStatusDisplayer(getResources(), merlinsBeard);

        findViewById(R.id.current_status).setOnClickListener(networkStatusOnClick);
        findViewById(R.id.wifi_connected).setOnClickListener(wifiConnectedOnClick);
        findViewById(R.id.mobile_connected).setOnClickListener(mobileConnectedOnClick);
        findViewById(R.id.network_subtype).setOnClickListener(networkSubtypeOnClick);
    }

    private final View.OnClickListener networkStatusOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (merlinsBeard.isConnected()) {
                networkStatusDisplayer.displayPositiveMessage(R.string.current_status_network_connected, viewToAttachDisplayerTo);
            } else {
                networkStatusDisplayer.displayNegativeMessage(R.string.current_status_network_disconnected, viewToAttachDisplayerTo);
            }
        }
    };

    private final View.OnClickListener wifiConnectedOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (merlinsBeard.isConnectedToWifi()) {
                networkStatusDisplayer.displayPositiveMessage(R.string.wifi_connected, viewToAttachDisplayerTo);
            } else {
                networkStatusDisplayer.displayNegativeMessage(R.string.wifi_disconnected, viewToAttachDisplayerTo);
            }
        }
    };

    private final View.OnClickListener mobileConnectedOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (merlinsBeard.isConnectedToMobileNetwork()) {
                networkStatusDisplayer.displayPositiveMessage(R.string.mobile_connected, viewToAttachDisplayerTo);
            } else {
                networkStatusDisplayer.displayNegativeMessage(R.string.mobile_disconnected, viewToAttachDisplayerTo);
            }
        }
    };

    private final View.OnClickListener networkSubtypeOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            networkStatusDisplayer.displayNetworkSubtype(viewToAttachDisplayerTo);
        }
    };

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerConnectable(this);
        registerDisconnectable(this);
        registerBindable(this);
    }

    @Override
    public void onBind(NetworkStatus networkStatus) {
        if (!networkStatus.isAvailable()) {
            onDisconnect();
        }
    }

    @Override
    public void onConnect() {
        networkStatusDisplayer.displayPositiveMessage(R.string.connected, viewToAttachDisplayerTo);
    }

    @Override
    public void onDisconnect() {
        networkStatusDisplayer.displayNegativeMessage(R.string.disconnected, viewToAttachDisplayerTo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStatusDisplayer.reset();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStatusDisplayer = null;
    }
}
