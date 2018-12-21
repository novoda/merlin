package com.novoda.java.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.novoda.demo.resources.CommonDemoActivity;
import com.novoda.merlin.contracts.MerlinsBeard;

public class DemoActivity extends CommonDemoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MerlinsBeard merlinsBeard = MerlinsBeard.create(this);

        currentStatus().setOnClickListener(view -> {
            if (merlinsBeard.isConnected()) {
                Toast.makeText(this, R.string.current_status_network_connected, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.current_status_network_disconnected, Toast.LENGTH_SHORT).show();
            }
        });

        hasInternetAccess().setOnClickListener(view -> {
            if (merlinsBeard.hasInternetAccess()) {
                Toast.makeText(this, R.string.has_internet_access_true, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.has_internet_access_false, Toast.LENGTH_SHORT).show();
            }
        });

        wifiConnected().setOnClickListener(view -> {
            if (merlinsBeard.isConnectedToWifi()) {
                Toast.makeText(this, R.string.wifi_connected, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.wifi_disconnected, Toast.LENGTH_SHORT).show();
            }
        });

        mobileConnected().setOnClickListener(view -> {
            if (merlinsBeard.isConnectedToMobileNetwork()) {
                Toast.makeText(this, R.string.mobile_connected, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.mobile_disconnected, Toast.LENGTH_SHORT).show();
            }
        });

        networkSubtype().setOnClickListener(view -> {
            String mobileNetworkSubtype = merlinsBeard.mobileNetworkSubtype();
            if (mobileNetworkSubtype.isEmpty()) {
                Toast.makeText(this, R.string.subtype_not_available, Toast.LENGTH_SHORT).show();
            } else {
                String message = getResources().getString(R.string.subtype_value, mobileNetworkSubtype);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        nextActivity().setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DemoActivity.class);
            startActivity(intent);
        });
    }
}
