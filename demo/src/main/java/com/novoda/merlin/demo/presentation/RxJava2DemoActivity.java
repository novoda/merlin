package com.novoda.merlin.demo.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.novoda.merlin.MerlinFlowable;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.demo.R;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RxJava2DemoActivity extends AppCompatActivity {

    private NetworkStatusDisplayer networkStatusDisplayer;
    private MerlinsBeard merlinsBeard;
    private CompositeDisposable disposables;
    private View viewToAttachDisplayerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewToAttachDisplayerTo = findViewById(R.id.displayerAttachableView);
        merlinsBeard = new MerlinsBeard.Builder()
                .build(this);
        networkStatusDisplayer = new NetworkStatusDisplayer(getResources(), merlinsBeard);
        disposables = new CompositeDisposable();

        findViewById(R.id.current_status).setOnClickListener(networkStatusOnClick);
        findViewById(R.id.has_internet_access).setOnClickListener(hasInternetAccessClick);
        findViewById(R.id.wifi_connected).setOnClickListener(wifiConnectedOnClick);
        findViewById(R.id.mobile_connected).setOnClickListener(mobileConnectedOnClick);
        findViewById(R.id.network_subtype).setOnClickListener(networkSubtypeOnClick);
        findViewById(R.id.next_activity).setOnClickListener(nextActivityOnClick);
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

    private final View.OnClickListener hasInternetAccessClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            merlinsBeard.hasInternetAccess(new MerlinsBeard.InternetAccessCallback() {
                @Override
                public void onResult(boolean hasAccess) {
                    if (hasAccess) {
                        networkStatusDisplayer.displayPositiveMessage(R.string.has_internet_access_true, viewToAttachDisplayerTo);
                    } else {
                        networkStatusDisplayer.displayNegativeMessage(R.string.has_internet_access_false, viewToAttachDisplayerTo);
                    }
                }
            });
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

    private final View.OnClickListener nextActivityOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), RxJava2DemoActivity.class);
            startActivity(intent);
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
