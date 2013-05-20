package com.novoda.merlin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.novoda.merlin.receiver.event.ConnectionEventPackager;
import com.novoda.merlin.receiver.event.ConnectivityChangeEvent;
import com.novoda.merlin.service.MerlinService;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && connectivityAction(intent)) {
            ConnectivityChangeEvent connectivityChangedEvent = ConnectionEventPackager.from(intent);
            notifyMerlinService(context, connectivityChangedEvent);
        }
    }

    private boolean connectivityAction(Intent intent) {
        return ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction());
    }

    private void notifyMerlinService(Context context, ConnectivityChangeEvent connectivityChangedEvent) {
        MerlinService merlinService = getMerlinService(context);
        if (isAvailable(merlinService)) {
            merlinService.onConnectivityChanged(connectivityChangedEvent);
        }
    }

    private static boolean isAvailable(Object object) {
        return object != null;
    }

    protected MerlinService getMerlinService(Context context) {
        IBinder binder = peekService(context, new Intent(context, MerlinService.class));
        if (isAvailable(binder)) {
            return ((MerlinService.LocalBinder) binder).getService();
        }
        return null;
    }

}
