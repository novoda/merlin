package com.novoda.merlin.demo.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.demo.R;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusCroutonDisplayer;
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer;
import com.novoda.merlin.rxjava2.MerlinFlowable;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RxJava2DemoActivity extends Activity {

    private NetworkStatusDisplayer networkStatusDisplayer;
    private MerlinsBeard merlinsBeard;
    private Toast toast;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        networkStatusDisplayer = new NetworkStatusCroutonDisplayer(this);
        merlinsBeard = MerlinsBeard.from(this);
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
         Disposable merlinDisposable = MerlinFlowable.from(this).subscribe(new Consumer<NetworkStatus.State>() {
             @Override
             public void accept(@NonNull NetworkStatus.State state) throws
                                                                    Exception {
                 if (NetworkStatus.State.AVAILABLE == state) {
                     networkStatusDisplayer.displayConnected();
                 } else {
                     networkStatusDisplayer.displayDisconnected();
                 }
             }
         });
        disposables.add(merlinDisposable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStatusDisplayer.reset();
        disposables.clear();
    }

}
