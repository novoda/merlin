package com.novoda.demo.resources;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CommonDemoActivity extends AppCompatActivity {

    private View currentStatus;
    private View hasInternetAccess;
    private View wifiConnected;
    private View mobileConnected;
    private View networkSubtype;
    private View nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        currentStatus = findViewById(R.id.current_status);
        hasInternetAccess = findViewById(R.id.has_internet_access);
        wifiConnected = findViewById(R.id.wifi_connected);
        mobileConnected = findViewById(R.id.mobile_connected);
        networkSubtype = findViewById(R.id.network_subtype);
        nextActivity = findViewById(R.id.next_activity);
    }

    protected View currentStatus() {
        return currentStatus;
    }

    protected View hasInternetAccess() {
        return hasInternetAccess;
    }

    protected View wifiConnected() {
        return wifiConnected;
    }

    protected View mobileConnected() {
        return mobileConnected;
    }

    protected View networkSubtype() {
        return networkSubtype;
    }

    protected View nextActivity() {
        return nextActivity;
    }
}
