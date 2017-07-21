package com.novoda.merlin.demo.presentation.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.logger.Logger;

public abstract class MerlinActivity extends AppCompatActivity {

    private DemoLogHandle logHandle;
    protected Merlin merlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logHandle = new DemoLogHandle();
        merlin = createMerlin();
    }

    protected abstract Merlin createMerlin();

    protected void registerConnectable(Connectable connectable) {
        merlin.registerConnectable(connectable);
    }

    protected void registerDisconnectable(Disconnectable disconnectable) {
        merlin.registerDisconnectable(disconnectable);
    }

    protected void registerBindable(Bindable bindable) {
        merlin.registerBindable(bindable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.attach(logHandle);
        merlin.bind();
    }

    @Override
    protected void onStop() {
        super.onStop();
        merlin.unbind();
        Logger.detach(logHandle);
    }

    private static class DemoLogHandle implements Logger.LogHandle {

        private static final String TAG = "DemoLogHandle";

        @Override
        public void v(Object... message) {
            Log.v(TAG, message[0].toString());
        }

        @Override
        public void i(Object... message) {
            Log.i(TAG, message[0].toString());
        }

        @Override
        public void d(Object... msg) {
            Log.d(TAG, msg[0].toString());
        }

        @Override
        public void d(Throwable throwable, Object... message) {
            Log.d(TAG, message[0].toString(), throwable);
        }

        @Override
        public void w(Object... message) {
            Log.w(TAG, message[0].toString());
        }

        @Override
        public void w(Throwable throwable, Object... message) {
            Log.w(TAG, message[0].toString(), throwable);
        }

        @Override
        public void e(Object... message) {
            Log.e(TAG, message[0].toString());
        }

        @Override
        public void e(Throwable throwable, Object... message) {
            Log.e(TAG, message[0].toString(), throwable);
        }
    }

}
