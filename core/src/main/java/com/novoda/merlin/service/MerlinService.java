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
        connectivityChangesRegister.unregister();
        connectivityChangesForwarder = null;
        connectivityChangesRegister = null;
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

        LocalBinder setConnectivityChangesRegister(ConnectivityChangesRegister connectivityChangesRegister) {
            MerlinService.this.connectivityChangesRegister = connectivityChangesRegister;
            return this;
        }

        LocalBinder setConnectivityChangesForwarder(ConnectivityChangesForwarder connectivityChangesForwarder) {
            MerlinService.this.connectivityChangesForwarder = connectivityChangesForwarder;
            return this;
        }

        void onBindComplete() {
            if (MerlinService.this.connectivityChangesRegister == null) {
                throw new IllegalStateException("setConnectivityChangesRegister must be called.");
            }

            if (MerlinService.this.connectivityChangesForwarder == null) {
                throw new IllegalStateException("setConnectivityChangesForwarder must be called.");
            }

            MerlinService.this.start();
        }
    }

}
