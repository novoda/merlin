package com.novoda.merlin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

public class ConnectivityReceiver extends BroadcastReceiver {

    private final ConnectivityReceiverConnectivityChangeNotifier connectivityReceiverConnectivityChangeNotifier;

    public ConnectivityReceiver() {
        MerlinsBeardCreator merlinsBeardCreator = new MerlinsBeardCreator() {
            @Override
            public MerlinsBeard createMerlinsBeard(Context context) {
                return MerlinsBeard.from(context);
            }
        };

        MerlinBinderRetriever merlinBinderRetriever = new MerlinBinderRetriever() {
            @Override
            public MerlinService.ConnectivityChangesNotifier retrieveConnectivityChangesNotifier(Context context) {
                IBinder iBinder = peekService(context, new Intent(context, MerlinService.class));
                if (iBinder instanceof MerlinService.ConnectivityChangesNotifier) {
                    return (MerlinService.ConnectivityChangesNotifier) iBinder;
                }
                return null;
            }
        };

        ConnectivityChangeEventCreator creator = new ConnectivityChangeEventCreator();
        connectivityReceiverConnectivityChangeNotifier = new ConnectivityReceiverConnectivityChangeNotifier(merlinsBeardCreator, merlinBinderRetriever, creator);
    }

    ConnectivityReceiver(ConnectivityReceiverConnectivityChangeNotifier connectivityReceiverConnectivityChangeNotifier) {
        this.connectivityReceiverConnectivityChangeNotifier = connectivityReceiverConnectivityChangeNotifier;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        connectivityReceiverConnectivityChangeNotifier.notify(context, intent);
    }

    interface MerlinBinderRetriever {

        @Nullable
        MerlinService.ConnectivityChangesNotifier retrieveConnectivityChangesNotifier(Context context);
    }

    interface MerlinsBeardCreator {

        MerlinsBeard createMerlinsBeard(Context context);
    }

}
