package com.novoda.merlin.demo.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.novoda.merlin.demo.R;

public class HomeActivity extends AppCompatActivity {

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
