package com.novoda.merlin.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.novoda.merlin.Endpoint;
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
            merlinServiceConnection = new MerlinServiceConnection(listenerHolder, endpoint, validator);
        }
        Intent intent = new Intent(context, MerlinService.class);
        context.bindService(intent, merlinServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        if (connectionIsAvailable()) {
            context.unbindService(merlinServiceConnection);
            merlinServiceConnection = null;
        }
    }

    private boolean connectionIsAvailable() {
        return merlinServiceConnection != null;
    }

    private static class MerlinServiceConnection implements ServiceConnection {

        private final ListenerHolder listenerHolder;
        private final Endpoint endpoint;
        private final ResponseCodeValidator validator;

        private MerlinService merlinService;

        MerlinServiceConnection(ListenerHolder listenerHolder, Endpoint endpoint, ResponseCodeValidator validator) {
            this.listenerHolder = listenerHolder;
            this.endpoint = endpoint;
            this.validator = validator;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Logger.d("onServiceConnected");
            merlinService = ((MerlinService.LocalBinder) binder).getService();
            merlinService.setConnectListener(listenerHolder.connectListener);
            merlinService.setDisconnectListener(listenerHolder.disconnectListener);
            merlinService.setBindStatusListener(listenerHolder.bindListener);
            merlinService.setHostname(endpoint, validator);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            merlinService = null;
        }

    }

    private static class ListenerHolder {

        private final DisconnectListener disconnectListener;
        private final ConnectListener connectListener;
        private final BindListener bindListener;

        public ListenerHolder(ConnectListener connectListener, DisconnectListener disconnectListener, BindListener bindListener) {
            this.connectListener = connectListener;
            this.disconnectListener = disconnectListener;
            this.bindListener = bindListener;
        }

    }

}
