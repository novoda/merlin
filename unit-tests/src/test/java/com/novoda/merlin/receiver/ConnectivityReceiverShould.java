package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.novoda.merlin.MerlinRobolectricTestRunner;
import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MerlinRobolectricTestRunner.class)
public class ConnectivityReceiverShould {

    private ConnectivityReceiver connectivityReceiver;

    Context context;
    @Mock
    MerlinService merlinService;

    @Before
    public void setUp() throws Exception {
        context = Robolectric.application;
        initMocks(this);
        connectivityReceiver = new ConnectivityReceiver() {
            @Override
            protected MerlinService getMerlinService(Context context) {
                return merlinService;
            }
        };
    }

    @Test
    public void notNotifyTheMerlinServiceOnNullIntents() throws Exception {
        connectivityReceiver.onReceive(context, null);

        verifyZeroInteractions(merlinService);
    }

    @Test
    public void notNotifyTheMerlinServiceOnNonConnectivityIntents() throws Exception {
        Intent intent = new Intent();
        intent.setAction("notNotifyTheMerlinServiceOnNonConnectivityIntents");
        connectivityReceiver.onReceive(context, intent);

        verifyZeroInteractions(merlinService);
    }

    @Test
    public void notifyTheMerlinServiceOnValidConnectivityIntents() throws Exception {
        Intent intent = new Intent();
        intent.setAction(ConnectivityManager.CONNECTIVITY_ACTION);

        connectivityReceiver.onReceive(context, intent);

        verify(merlinService).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void notExplodeWhenTheMerlinServiceIsNull() throws Exception {
        Intent intent = new Intent();
        intent.setAction(ConnectivityManager.CONNECTIVITY_ACTION);
        merlinService = null;

        connectivityReceiver.onReceive(context, intent);
    }

}
