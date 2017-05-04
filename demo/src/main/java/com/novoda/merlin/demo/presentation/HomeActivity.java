package com.novoda.merlin.demo.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.novoda.merlin.demo.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        findViewById(R.id.demo_launch_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this, DemoActivity.class));
                    }
                }
        );

        findViewById(R.id.rx_java_demo_launch_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this, RxJavaDemoActivity.class));
                    }
                }
        );

        findViewById(R.id.rx_java2_demo_launch_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this, RxJava2DemoActivity.class));
                    }
                }
        );
    }
}
