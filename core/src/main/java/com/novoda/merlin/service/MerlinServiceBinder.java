package com.novoda.merlin.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.novoda.merlin.Endpoint;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.receiver.ConnectivityChangesRegister;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;
import com.novoda.support.Logger;

public class MerlinServiceBinder {

    private final Context context;
    private final ListenerHolder listenerHolder;

    private ResponseCodeValidator validator;
    private MerlinServiceConnection merlinServiceConnection;
    private Endpoint endpoint;

    public MerlinServiceBinder(Context context, ConnectListener connectListener, DisconnectListener disconnectListener,
                               BindListener bindListener, Endpoint endpoint, ResponseCodeValidator validator) {
        this.validator = validator;
        listenerHolder = new ListenerHolder(connectListener, disconnectListener, bindListener);
        this.context = context;
        this.endpoint = endpoint;
    }

    public void setEndpoint(Endpoint endpoint, ResponseCodeValidator validator) {
        this.endpoint = endpoint;
        this.validator = validator;
    }

    public void bindService() {
        if (merlinServiceConnection == null) {
            merlinServiceConnection = new MerlinServiceConnection(context, listenerHolder, endpoint, validator);
        }
        Intent intent = new Intent(context, MerlinService.class);
        context.bindService(intent, merlinServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        if (MerlinService.isBound()) {
            context.unbindService(merlinServiceConnection);
            merlinServiceConnection = null;
        }
    }

    private static class MerlinServiceConnection implements ServiceConnection {

        private final Context context;
        private final ListenerHolder listenerHolder;
        private final Endpoint endpoint;
        private final ResponseCodeValidator validator;

        MerlinServiceConnection(Context context, ListenerHolder listenerHolder, Endpoint endpoint, ResponseCodeValidator validator) {
            this.context = context;
            this.listenerHolder = listenerHolder;
            this.endpoint = endpoint;
            this.validator = validator;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Logger.d("onServiceConnected");
            MerlinService.LocalBinder merlinServiceBinder = ((MerlinService.LocalBinder) binder);
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            ConnectivityChangesRegister connectivityChangesRegister = new ConnectivityChangesRegister(
                    context,
                    connectivityManager,
                    new AndroidVersion()
            );

            merlinServiceBinder.setConnectivityChangesRegister(connectivityChangesRegister);
            merlinServiceBinder.setHostPinger(endpoint, validator);
            merlinServiceBinder.setNetworkStatusRetriever(new NetworkStatusRetriever(MerlinsBeard.from(context)));
            merlinServiceBinder.setConnectListener(listenerHolder.connectListener);
            merlinServiceBinder.setDisconnectListener(listenerHolder.disconnectListener);
            merlinServiceBinder.setBindListener(listenerHolder.bindListener);
            merlinServiceBinder.onBindComplete();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // do nothing.
        }

    }

    private static class ListenerHolder {

        private final DisconnectListener disconnectListener;
        private final ConnectListener connectListener;
        private final BindListener bindListener;

        ListenerHolder(ConnectListener connectListener, DisconnectListener disconnectListener, BindListener bindListener) {
            this.connectListener = connectListener;
            this.disconnectListener = disconnectListener;
            this.bindListener = bindListener;
        }

    }

}
