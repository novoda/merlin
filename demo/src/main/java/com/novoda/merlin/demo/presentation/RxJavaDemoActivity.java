package com.novoda.merlin.demo.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.novoda.merlin.rxjava.MerlinObservable;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.demo.R;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusCroutonDisplayer;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class RxJavaDemoActivity extends Activity {

    private NetworkStatusDisplayer networkStatusDisplayer;
    private MerlinsBeard merlinsBeard;
    private Toast toast;
    private CompositeSubscription subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        networkStatusDisplayer = new NetworkStatusCroutonDisplayer(this);
        merlinsBeard = MerlinsBeard.from(this);
        subscriptions = new CompositeSubscription();

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
    protected void onResume() {
        super.onResume();
        Subscription merlinSubscription = MerlinObservable.from(this)
                                                          .distinctUntilChanged()
                                                          .subscribe(new NetworkAction(networkStatusDisplayer));
        subscriptions.add(merlinSubscription);
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStatusDisplayer.reset();
        subscriptions.clear();
    }

    private static class NetworkAction implements Action1<NetworkStatus> {

        private final NetworkStatusDisplayer networkStatusDisplayer;

        NetworkAction(NetworkStatusDisplayer networkStatusDisplayer) {
            this.networkStatusDisplayer = networkStatusDisplayer;
        }

        @Override
        public void call(NetworkStatus networkStatus) {
            if (networkStatus.isAvailable()) {
                networkStatusDisplayer.displayConnected();
            } else {
                networkStatusDisplayer.displayDisconnected();
            }
        }
    }

}
