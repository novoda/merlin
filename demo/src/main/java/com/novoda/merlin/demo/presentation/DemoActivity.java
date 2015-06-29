package com.novoda.merlin.demo.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.demo.R;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusCroutonDisplayer;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer;
import com.novoda.merlin.demo.presentation.base.MerlinActivity;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import static com.novoda.merlin.MerlinsBeard.NetworkType.WIFI;

public class DemoActivity extends MerlinActivity implements Connectable, Disconnectable, Bindable {

    private NetworkStatusDisplayer networkStatusDisplayer;
    private MerlinsBeard merlinsBeard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        networkStatusDisplayer = new NetworkStatusCroutonDisplayer(this);
        merlinsBeard = MerlinsBeard.from(this);

        findViewById(R.id.current_status).setOnClickListener(networkStatusOnClick);
        findViewById(R.id.wifi_connected).setOnClickListener(wifiConnectedOnClick);
    }

    private final View.OnClickListener networkStatusOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (merlinsBeard.isConnected()) {
                showToast(R.string.toast_connected);
            } else {
                showToast(R.string.toast_disconnected);
            }
        }
    };

    private void showToast(int toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

    private final View.OnClickListener wifiConnectedOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (merlinsBeard.isConnectedTo(WIFI)) {
                showToast(R.string.toast_connected);
            } else {
                showToast(R.string.toast_disconnected);
            }
        }
    };

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .withLogging(true)
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
