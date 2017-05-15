package com.novoda.merlin.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

public class ConnectivityReceiver extends BroadcastReceiver {

    private final MerlinsBeardRetriever merlinsBeardRetriever;
    private final ServiceRetriever serviceRetriever;
    private final ConnectivityChangeEventCreator creator;

    public ConnectivityReceiver() {
        this.merlinsBeardRetriever = new MerlinsBeardRetriever() {
            @Override
            public MerlinsBeard getMerlinsBeard(Context context) {
                return MerlinsBeard.from(context);
            }
        };

        this.serviceRetriever = new ServiceRetriever() {
            @Override
            public MerlinService getService(Context context) {
                IBinder binder = peekService(context, new Intent(context, MerlinService.class));

                if (isAvailable(binder)) {
                    return ((MerlinService.LocalBinder) binder).getService();
                }
                return null;
            }

            private boolean isAvailable(Object object) {
                return object != null;
            }
        };

        this.creator = new ConnectivityChangeEventCreator();
    }

    ConnectivityReceiver(MerlinsBeardRetriever merlinsBeardRetriever,
                         ServiceRetriever serviceRetriever,
                         ConnectivityChangeEventCreator creator) {
        this.merlinsBeardRetriever = merlinsBeardRetriever;
        this.serviceRetriever = serviceRetriever;
        this.creator = creator;
    }

    // Lint thinks we're not calling getAction, but we actually do
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && connectivityAction(intent)) {
            MerlinsBeard merlinsBeard = merlinsBeardRetriever.getMerlinsBeard(context);
            ConnectivityChangeEvent connectivityChangeEvent = creator.createFrom(intent, merlinsBeard);
            notifyMerlinService(context, connectivityChangeEvent);
        }
    }

    private boolean connectivityAction(Intent intent) {
        return ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction());
    }

    private void notifyMerlinService(Context context, ConnectivityChangeEvent connectivityChangedEvent) {
        MerlinService merlinService = getMerlinService(context);
        if (merlinService == null) {
            return;
        }

        merlinService.onConnectivityChanged(connectivityChangedEvent);
    }

    interface ServiceRetriever {

        @Nullable
        MerlinService getService(Context context);
    }

    interface MerlinsBeardRetriever {

        MerlinsBeard getMerlinsBeard(Context context);
    }

    private MerlinService getMerlinService(Context context) {
        return serviceRetriever.getService(context);
    }

}
