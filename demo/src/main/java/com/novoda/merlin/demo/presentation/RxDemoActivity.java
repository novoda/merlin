package com.novoda.merlin.demo.presentation;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.demo.R;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusCroutonDisplayer;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer;
import com.novoda.merlin.demo.presentation.base.RxMerlinActivity;

import rx.Subscription;
import rx.functions.Action1;

public class RxDemoActivity extends RxMerlinActivity {

    private NetworkStatusDisplayer networkStatusDisplayer;
    private MerlinsBeard merlinsBeard;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        networkStatusDisplayer = new NetworkStatusCroutonDisplayer(this);
        merlinsBeard = MerlinsBeard.from(this);

        findViewById(R.id.current_status).setOnClickListener(networkStatusOnClick);
        findViewById(R.id.wifi_connected).setOnClickListener(wifiConnectedOnClick);
        findViewById(R.id.mobile_connected).setOnClickListener(mobileConnectedOnClick);
        findViewById(R.id.network_subtype).setOnClickListener(networkSubtypeOnClick);
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

    private final View.OnClickListener wifiConnectedOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (merlinsBeard.isConnectedToWifi()) {
                showToast(R.string.toast_connected);
            } else {
                showToast(R.string.toast_disconnected);
            }
        }
    };

    private final View.OnClickListener mobileConnectedOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (merlinsBeard.isConnectedToMobileNetwork()) {
                showToast(R.string.toast_connected);
            } else {
                showToast(R.string.toast_disconnected);
            }
        }
    };

    private final View.OnClickListener networkSubtypeOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            showToast(merlinsBeard.getMobileNetworkSubtypeName());
        }
    };

    private void showToast(@StringRes int toastText) {
        String message = getString(toastText);
        showToast(message);
    }

    private void showToast(String toastText) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withRxCallbacks()
                .withLogging(true)
                .build(this);
    }

    @Override
    protected Subscription createRxSubscription() {
        return connectionStatusObservable.subscribe(
                new Action1<Boolean>() {
                    @Override
                    public void call(Boolean connected) {
                        if (connected) {
                            networkStatusDisplayer.displayConnected();
                        } else {
                            networkStatusDisplayer.displayDisconnected();
                        }
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStatusDisplayer.reset();
    }

}
