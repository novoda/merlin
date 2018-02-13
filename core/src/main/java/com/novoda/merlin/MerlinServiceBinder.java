package com.novoda.merlin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;

class MerlinServiceBinder {

    private final Context context;
    private final ListenerHolder listenerHolder;

    private ResponseCodeValidator validator;
    private MerlinServiceConnection merlinServiceConnection;
    private Endpoint endpoint;

    MerlinServiceBinder(Context context, ConnectCallbackManager connectCallbackManager, DisconnectCallbackManager disconnectCallbackManager,
                        BindCallbackManager bindCallbackManager, Endpoint endpoint, ResponseCodeValidator validator) {
        this.validator = validator;
        listenerHolder = new ListenerHolder(connectCallbackManager, disconnectCallbackManager, bindCallbackManager);
        this.context = context;
        this.endpoint = endpoint;
    }

    void setEndpoint(Endpoint endpoint, ResponseCodeValidator validator) {
        this.endpoint = endpoint;
        this.validator = validator;
    }

    void bindService() {
        if (merlinServiceConnection == null) {
            merlinServiceConnection = new MerlinServiceConnection(context, listenerHolder, endpoint, validator);
        }
        Intent intent = new Intent(context, MerlinService.class);
        context.bindService(intent, merlinServiceConnection, Context.BIND_AUTO_CREATE);
    }

    void unbind() {
        if (MerlinService.isBound() && merlinServiceConnection != null) {
            context.unbindService(merlinServiceConnection);
            context.stopService(new Intent(context, MerlinService.class));
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
            ConnectivityChangeEventExtractor connectivityChangeEventExtractor = new ConnectivityChangeEventExtractor(connectivityManager);
            ConnectivityChangesRegister connectivityChangesRegister = new ConnectivityChangesRegister(
                    context,
                    connectivityManager,
                    new AndroidVersion(),
                    connectivityChangeEventExtractor
            );
            NetworkStatusRetriever networkStatusRetriever = new NetworkStatusRetriever(MerlinsBeard.from(context));
            EndpointPinger endpointPinger = EndpointPinger.withCustomEndpointAndValidation(endpoint, validator);
            ConnectivityChangesForwarder connectivityChangesForwarder = new ConnectivityChangesForwarder(
                    networkStatusRetriever,
                    listenerHolder.disconnectCallbackManager,
                    listenerHolder.connectCallbackManager,
                    listenerHolder.bindCallbackManager,
                    endpointPinger
            );

            merlinServiceBinder.setConnectivityChangesRegister(connectivityChangesRegister);
            merlinServiceBinder.setConnectivityChangesForwarder(connectivityChangesForwarder);
            merlinServiceBinder.onBindComplete();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // do nothing.
        }

    }

    private static class ListenerHolder {

        private final DisconnectCallbackManager disconnectCallbackManager;
        private final ConnectCallbackManager connectCallbackManager;
        private final BindCallbackManager bindCallbackManager;

        ListenerHolder(ConnectCallbackManager connectCallbackManager, DisconnectCallbackManager disconnectCallbackManager, BindCallbackManager bindCallbackManager) {
            this.connectCallbackManager = connectCallbackManager;
            this.disconnectCallbackManager = disconnectCallbackManager;
            this.bindCallbackManager = bindCallbackManager;
        }

    }

}
