package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

class ConnectivityChangeNotifier {

    private final ConnectivityReceiver.MerlinsBeardRetriever merlinsBeardRetriever;
    private final ConnectivityReceiver.MerlinServiceRetriever merlinServiceRetriever;
    private final ConnectivityChangeEventCreator creator;

    ConnectivityChangeNotifier(ConnectivityReceiver.MerlinsBeardRetriever merlinsBeardRetriever,
                               ConnectivityReceiver.MerlinServiceRetriever merlinServiceRetriever,
                               ConnectivityChangeEventCreator creator) {

        this.merlinsBeardRetriever = merlinsBeardRetriever;
        this.merlinServiceRetriever = merlinServiceRetriever;
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
        MerlinService service = merlinServiceRetriever.getService(context);

        if (service == null) {
            return;
        }

        service.onConnectivityChanged(connectivityChangeEvent);
    }

    private boolean connectivityAction(Intent intent) {
        return ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction());
    }

}
