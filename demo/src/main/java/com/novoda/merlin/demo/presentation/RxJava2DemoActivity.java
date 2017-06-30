package com.novoda.merlin.demo.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.demo.R;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer;
import com.novoda.merlin.rxjava2.MerlinFlowable;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RxJava2DemoActivity extends Activity {

    private NetworkStatusDisplayer networkStatusDisplayer;
    private MerlinsBeard merlinsBeard;
    private CompositeDisposable disposables;
    private View viewToAttachDisplayerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewToAttachDisplayerTo = findViewById(R.id.displayerAttachableView);
        merlinsBeard = MerlinsBeard.from(this);
        networkStatusDisplayer = new NetworkStatusDisplayer(getResources(), merlinsBeard);
        disposables = new CompositeDisposable();

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
    protected void onResume() {
        super.onResume();
        Disposable merlinDisposable = MerlinFlowable.from(this)
                .distinctUntilChanged()
                .subscribe(new NetworkConsumer(networkStatusDisplayer, viewToAttachDisplayerTo));
        disposables.add(merlinDisposable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStatusDisplayer.reset();
        disposables.clear();
    }

    private static class NetworkConsumer implements Consumer<NetworkStatus> {

        private final NetworkStatusDisplayer networkStatusDisplayer;
        private final View viewToAttachDisplayerTo;

        NetworkConsumer(NetworkStatusDisplayer networkStatusDisplayer, View viewToAttachDisplayerTo) {
            this.networkStatusDisplayer = networkStatusDisplayer;
            this.viewToAttachDisplayerTo = viewToAttachDisplayerTo;
        }

        @Override
        public void accept(@NonNull NetworkStatus networkStatus) throws Exception {
            if (networkStatus.isAvailable()) {
                networkStatusDisplayer.displayPositiveMessage(R.string.connected, viewToAttachDisplayerTo);
            } else {
                networkStatusDisplayer.displayNegativeMessage(R.string.disconnected, viewToAttachDisplayerTo);
            }
        }
    }

}
