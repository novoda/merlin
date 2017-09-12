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
        assertDependenciesBound();
        connectivityChangesForwarder.forwardInitialNetworkStatus();
        connectivityChangesRegister.register(connectivityChangesListener);
    }

    private void assertDependenciesBound() {
        if (MerlinService.this.connectivityChangesRegister == null) {
            throw MerlinServiceDependencyMissingException.missing(ConnectivityChangesRegister.class);
        }

        if (MerlinService.this.connectivityChangesForwarder == null) {
            throw MerlinServiceDependencyMissingException.missing(ConnectivityChangesForwarder.class);
        }
    }

    private final ConnectivityChangesListener connectivityChangesListener = new ConnectivityChangesListener() {
        @Override
        public void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent) {
            connectivityChangesForwarder.forward(connectivityChangeEvent);
        }
    };

    private void notify(ConnectivityChangeEvent connectivityChangeEvent) {
        assertDependenciesBound();

        connectivityChangesForwarder.forward(connectivityChangeEvent);
    }

    public interface ConnectivityChangesListener {
        void onConnectivityChanged(ConnectivityChangeEvent connectivityChangeEvent);
    }

    public interface ConnectivityChangesNotifier {

        boolean canNotify();

        void notify(ConnectivityChangeEvent connectivityChangeEvent);

    }

    class LocalBinder extends Binder implements ConnectivityChangesNotifier {

        @Override
        public boolean canNotify() {
            return MerlinService.isBound();
        }

        @Override
        public void notify(ConnectivityChangeEvent connectivityChangeEvent) {
            MerlinService.this.notify(connectivityChangeEvent);
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
