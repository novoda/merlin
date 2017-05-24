package com.novoda.merlin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.receiver.ConnectivityChangesRegister;

public class MerlinService extends Service {

    private static boolean isBound;

    private final IBinder binder = new LocalBinder();

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
        return super.onUnbind(intent);
    }

    private void start() {
        connectivityChangesForwarder.notifyOfInitialNetworkStatus();
        connectivityChangesRegister.register(connectivityChangesListener);
    }

    private final ConnectivityChangesListener connectivityChangesListener = new ConnectivityChangesListener() {
        @Override
        public void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent) {
            connectivityChangesForwarder.notifyOf(connectivityChangeEvent);
        }
    };

    public interface ConnectivityChangesListener {
        void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent);
    }

    public class LocalBinder extends Binder {

        public ConnectivityChangesListener getConnectivityChangedListener() {
            return MerlinService.this.connectivityChangesListener;
        }

        void setConnectivityChangesRegister(ConnectivityChangesRegister connectivityChangesRegister) {
            MerlinService.this.connectivityChangesRegister = connectivityChangesRegister;
        }

        void setConnectivityChangesForwarder(ConnectivityChangesForwarder connectivityChangesForwarder) {
            MerlinService.this.connectivityChangesForwarder = connectivityChangesForwarder;
        }

        void onBindComplete() {
            MerlinService.this.start();
        }
    }

}
