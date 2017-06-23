package com.novoda.merlin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.receiver.ConnectivityChangesRegister;

public class MerlinService extends Service {

    private static boolean isBound;

    private IBinder binder = new LocalBinder();

    private ConnectivityChangesRegister connectivityChangesRegister;
    private ConnectivityChangesForwarder connectivityChangesForwarder;

    public static boolean isBound() {
        return isBound;
    }

    @Override
    public IBinder onBind(Intent intent) {
        isBound = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBound = false;
        if (connectivityChangesRegister != null) {
            connectivityChangesRegister.unregister();
            connectivityChangesRegister = null;

        }

        if (connectivityChangesForwarder != null) {
            connectivityChangesForwarder = null;
        }

        binder = null;
        return super.onUnbind(intent);
    }

    private void start() {
        connectivityChangesForwarder.forwardInitialNetworkStatus();
        connectivityChangesRegister.register(connectivityChangesListener);
    }

    private final ConnectivityChangesListener connectivityChangesListener = new ConnectivityChangesListener() {
        @Override
        public void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent) {
            connectivityChangesForwarder.forward(connectivityChangeEvent);
        }
    };

    public interface ConnectivityChangesListener {
        void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent);
    }

    public class LocalBinder extends Binder {

        public ConnectivityChangesListener connectivityChangesListener() {
            return MerlinService.this.connectivityChangesListener;
        }

        void setConnectivityChangesRegister(ConnectivityChangesRegister connectivityChangesRegister) {
            MerlinService.this.connectivityChangesRegister = connectivityChangesRegister;
        }

        void setConnectivityChangesForwarder(ConnectivityChangesForwarder connectivityChangesForwarder) {
            MerlinService.this.connectivityChangesForwarder = connectivityChangesForwarder;
        }

        void onBindComplete() {
            assertDependenciesBound();
            MerlinService.this.start();
        }

        private void assertDependenciesBound() {
            if (MerlinService.this.connectivityChangesRegister == null) {
                throw LocalBinderDependencyMissingException.missing(ConnectivityChangesRegister.class);
            }

            if (MerlinService.this.connectivityChangesForwarder == null) {
                throw LocalBinderDependencyMissingException.missing(ConnectivityChangesForwarder.class);
            }
        }
    }

}
