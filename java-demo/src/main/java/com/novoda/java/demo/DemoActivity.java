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
                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
            }
        });

        hasInternetAccess().setOnClickListener(view -> {
            if (merlinsBeard.hasInternetAccess()) {
                Toast.makeText(this, "Has internet access", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Does not have internet access", Toast.LENGTH_SHORT).show();
            }
        });

        wifiConnected().setOnClickListener(view -> {
            if (merlinsBeard.isConnectedToWifi()) {
                Toast.makeText(this, "Wifi connected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wifi disconnected", Toast.LENGTH_SHORT).show();
            }
        });

        mobileConnected().setOnClickListener(view -> {
            if (merlinsBeard.isConnectedToMobileNetwork()) {
                Toast.makeText(this, "Mobile connected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Mobile disconnected", Toast.LENGTH_SHORT).show();
            }
        });

        networkSubtype().setOnClickListener(view -> {
            String message = "Network subtype: " + merlinsBeard.mobileNetworkSubtype();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        nextActivity().setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DemoActivity.class);
            startActivity(intent);
        });
    }
}
