package com.novoda.merlin.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

public class MerlinServiceBinder {

    private final Context context;
    private final ListenerHolder listenerHolder;

    private Connection connection;
    private String endpoint;

    public MerlinServiceBinder(Context context, ConnectListener connectListener, DisconnectListener disconnectListener,
                               BindListener bindListener, String endpoint) {
        listenerHolder = new ListenerHolder(connectListener, disconnectListener, bindListener);
        this.context = context;
        this.endpoint = endpoint;
    }

    public void setEndpoint(String hostname) {
        this.endpoint = hostname;
    }

    public void bindService() {
        if (connection == null) {
            connection = new Connection(listenerHolder, endpoint);
        }
        Intent intent = new Intent(context, MerlinService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        if (connectionIsAvailable()) {
            context.unbindService(connection);
            connection = null;
        }
    }

    private boolean connectionIsAvailable() {
        return connection != null;
    }

    private static class Connection implements ServiceConnection {

        private final ListenerHolder listenerHolder;
        private final String endpoint;

        private MerlinService merlinService;

        Connection(ListenerHolder listenerHolder, String endpoint) {
            this.listenerHolder = listenerHolder;
            this.endpoint = endpoint;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MerlinLog.d("onServiceConnected");
            merlinService = ((MerlinService.LocalBinder) binder).getService();
            merlinService.setConnectListener(listenerHolder.connectListener);
            merlinService.setDisconnectListener(listenerHolder.disconnectListener);
            merlinService.setBindStatusListener(listenerHolder.bindListener);
            merlinService.setHostname(endpoint);
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
