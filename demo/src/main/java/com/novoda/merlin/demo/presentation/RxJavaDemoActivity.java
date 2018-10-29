package com.novoda.merlin.demo.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.novoda.merlin.MerlinObservable;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.demo.R;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class RxJavaDemoActivity extends AppCompatActivity {

    private NetworkStatusDisplayer networkStatusDisplayer;
    private MerlinsBeard merlinsBeard;
    private CompositeSubscription subscriptions;
    private View viewToAttachDisplayerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewToAttachDisplayerTo = findViewById(R.id.displayerAttachableView);
        merlinsBeard = new MerlinsBeard.Builder()
                .build(this);
        networkStatusDisplayer = new NetworkStatusDisplayer(getResources(), merlinsBeard);
        subscriptions = new CompositeSubscription();

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

    private final View.OnClickListener nextActivityOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), RxJavaDemoActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Subscription merlinSubscription = MerlinObservable.from(this)
                .distinctUntilChanged()
                .subscribe(new NetworkAction(networkStatusDisplayer, viewToAttachDisplayerTo));
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
        private final View viewToAttachDisplayerTo;

        NetworkAction(NetworkStatusDisplayer networkStatusDisplayer, View viewToAttachDisplayerTo) {
            this.networkStatusDisplayer = networkStatusDisplayer;
            this.viewToAttachDisplayerTo = viewToAttachDisplayerTo;
        }

        @Override
        public void call(NetworkStatus networkStatus) {
            if (networkStatus.isAvailable()) {
                networkStatusDisplayer.displayPositiveMessage(R.string.connected, viewToAttachDisplayerTo);
            } else {
                networkStatusDisplayer.displayNegativeMessage(R.string.disconnected, viewToAttachDisplayerTo);
            }
        }
    }

}
