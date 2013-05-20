package com.novoda.merlin.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.novoda.merlin.Log;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

public class MerlinServiceBinder {

    private final Context context;
    private final ListenerHolder listenerHolder;

    private Connection connection;
    private String hostname;

    public MerlinServiceBinder(Context context, ConnectListener connectListener, DisconnectListener disconnectListener,
                               BindListener bindListener) {
        listenerHolder = new ListenerHolder(connectListener, disconnectListener, bindListener);
        this.context = context;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void bindService() {
        Log.d("Bind Service");
        if (connection == null) {
            connection = new Connection(listenerHolder, hostname);
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
        private final String hostname;

        private MerlinService merlinService;

        Connection(ListenerHolder listenerHolder, String hostname) {
            this.listenerHolder = listenerHolder;
            this.hostname = hostname;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("onServiceConnected");
            merlinService = ((MerlinService.LocalBinder) binder).getService();
            merlinService.setConnectListener(listenerHolder.connectListener);
            merlinService.setDisconnectListener(listenerHolder.disconnectListener);
            merlinService.setBindStatusListener(listenerHolder.bindListener);
            merlinService.setHostname(hostname);
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
