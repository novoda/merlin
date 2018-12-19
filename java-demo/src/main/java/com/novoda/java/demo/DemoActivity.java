package com.novoda.java.demo;

import android.os.Bundle;
import android.widget.Toast;
import com.novoda.demo.resources.CommonDemoActivity;

public class DemoActivity extends CommonDemoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentStatus().setOnClickListener(view -> Toast.makeText(this, "Network status", Toast.LENGTH_SHORT).show());
        hasInternetAccess().setOnClickListener(view -> Toast.makeText(this, "Has internet access", Toast.LENGTH_SHORT).show());
        wifiConnected().setOnClickListener(view -> Toast.makeText(this, "Wifi connected", Toast.LENGTH_SHORT).show());
        mobileConnected().setOnClickListener(view -> Toast.makeText(this, "Mobile connected", Toast.LENGTH_SHORT).show());
        networkSubtype().setOnClickListener(view -> Toast.makeText(this, "Network subtype", Toast.LENGTH_SHORT).show());
        nextActivity().setOnClickListener(view -> Toast.makeText(this, "Navigate to next activity", Toast.LENGTH_SHORT).show());
    }
}
