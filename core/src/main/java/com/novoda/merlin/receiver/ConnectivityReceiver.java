package com.novoda.merlin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

public class ConnectivityReceiver extends BroadcastReceiver {

    private final ConnectivityChangeNotifier connectivityChangeNotifier;

    public ConnectivityReceiver() {
        MerlinsBeardRetriever merlinsBeardRetriever = new MerlinsBeardRetriever() {
            @Override
            public MerlinsBeard getMerlinsBeard(Context context) {
                return MerlinsBeard.from(context);
            }
        };

        MerlinBinderRetriever merlinBinderRetriever = new MerlinBinderRetriever() {
            @Override
            public IBinder getBinder(Context context) {
                return peekService(context, new Intent(context, MerlinService.class));
            }
        };

        ConnectivityChangeEventCreator creator = new ConnectivityChangeEventCreator();
        connectivityChangeNotifier = new ConnectivityChangeNotifier(merlinsBeardRetriever, merlinBinderRetriever, creator);
    }

    ConnectivityReceiver(ConnectivityChangeNotifier connectivityChangeNotifier) {
        this.connectivityChangeNotifier = connectivityChangeNotifier;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        connectivityChangeNotifier.notify(context, intent);
    }

    interface MerlinBinderRetriever {

        @Nullable
        IBinder getBinder(Context context);
    }

    interface MerlinsBeardRetriever {

        MerlinsBeard getMerlinsBeard(Context context);
    }

}
