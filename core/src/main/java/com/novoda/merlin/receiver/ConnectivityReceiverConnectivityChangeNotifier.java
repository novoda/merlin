package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.logger.Logger;
import com.novoda.merlin.service.MerlinService;

class ConnectivityReceiverConnectivityChangeNotifier {

    private final ConnectivityReceiver.MerlinsBeardCreator merlinsBeardCreator;
    private final ConnectivityReceiver.MerlinBinderRetriever merlinBinderRetriever;
    private final ConnectivityChangeEventCreator creator;

    ConnectivityReceiverConnectivityChangeNotifier(ConnectivityReceiver.MerlinsBeardCreator merlinsBeardCreator,
                                                   ConnectivityReceiver.MerlinBinderRetriever merlinBinderRetriever,
                                                   ConnectivityChangeEventCreator creator) {

        this.merlinsBeardCreator = merlinsBeardCreator;
        this.merlinBinderRetriever = merlinBinderRetriever;
        this.creator = creator;
    }

    void notify(Context context, Intent intent) {
        if (intent != null && connectivityActionMatchesActionFor(intent)) {
            MerlinsBeard merlinsBeard = merlinsBeardCreator.createMerlinsBeard(context);
            ConnectivityChangeEvent connectivityChangeEvent = creator.createFrom(intent, merlinsBeard);
            notifyMerlinService(context, connectivityChangeEvent);
        }
    }

    private void notifyMerlinService(Context context, ConnectivityChangeEvent connectivityChangeEvent) {
        MerlinService.ConnectivityChangesNotifier notifier = merlinBinderRetriever.retrieveConnectivityChangesNotifier(context);

        if (cannotNotify(notifier)) {
            Logger.d("Cannot notify " + MerlinService.ConnectivityChangesNotifier.class.getSimpleName());
            return;
        }
        notifier.notify(connectivityChangeEvent);

    }

    private boolean cannotNotify(MerlinService.ConnectivityChangesNotifier notifier) {
        return notifier == null || !notifier.canNotify();
    }

    private boolean connectivityActionMatchesActionFor(Intent intent) {
        return ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction());
    }

}
