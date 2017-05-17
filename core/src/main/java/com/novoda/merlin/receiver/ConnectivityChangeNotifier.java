package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

class ConnectivityChangeNotifier {

    private final ConnectivityReceiver.MerlinsBeardRetriever merlinsBeardRetriever;
    private final ConnectivityReceiver.MerlinBinderRetriever merlinBinderRetriever;
    private final ConnectivityChangeEventCreator creator;

    ConnectivityChangeNotifier(ConnectivityReceiver.MerlinsBeardRetriever merlinsBeardRetriever,
                               ConnectivityReceiver.MerlinBinderRetriever merlinBinderRetriever,
                               ConnectivityChangeEventCreator creator) {

        this.merlinsBeardRetriever = merlinsBeardRetriever;
        this.merlinBinderRetriever = merlinBinderRetriever;
        this.creator = creator;
    }

    void notify(Context context, Intent intent) {
        if (intent != null && connectivityAction(intent)) {
            MerlinsBeard merlinsBeard = merlinsBeardRetriever.getMerlinsBeard(context);
            ConnectivityChangeEvent connectivityChangeEvent = creator.createFrom(intent, merlinsBeard);
            notifyMerlinService(context, connectivityChangeEvent);
        }
    }

    private void notifyMerlinService(Context context, ConnectivityChangeEvent connectivityChangeEvent) {
        IBinder binder = merlinBinderRetriever.getBinder(context);

        if (merlinServiceAvailableFrom(binder)) {
            MerlinService merlinService = ((MerlinService.LocalBinder) binder).getService();
            merlinService.onConnectivityChanged(connectivityChangeEvent);
        }
    }

    private boolean merlinServiceAvailableFrom(IBinder binder) {
        return binder != null && binder instanceof MerlinService.LocalBinder && ((MerlinService.LocalBinder) binder).getService() != null;
    }

    private boolean connectivityAction(Intent intent) {
        return ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction());
    }

}
