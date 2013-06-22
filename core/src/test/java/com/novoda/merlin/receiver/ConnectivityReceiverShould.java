package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.novoda.merlin.receiver.event.ConnectivityChangeEvent;
import com.novoda.merlin.service.MerlinService;
import com.novoda.robolectric.NovodaRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(NovodaRobolectricTestRunner.class)
public class ConnectivityReceiverShould {

    private ConnectivityReceiver connectivityReceiver;

    @Mock Context context;
    @Mock MerlinService merlinService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        connectivityReceiver = new ConnectivityReceiver() {
            @Override
            protected MerlinService getMerlinService(Context context) {
                return merlinService;
            }
        };
    }

    @Test
    public void not_notify_the_merlin_service_on_null_intents() throws Exception {
        connectivityReceiver.onReceive(context, null);

        verifyZeroInteractions(merlinService);
    }

    @Test
    public void not_notify_the_merlin_service_on_non_connectivity_intents() throws Exception {
        Intent intent = new Intent();
        intent.setAction("not_notify_the_merlin_service_on_non_connectivity_intents");
        connectivityReceiver.onReceive(context, intent);

        verifyZeroInteractions(merlinService);
    }

    @Test
    public void notify_the_merlin_service_on_valid_connectivity_intents() throws Exception {
        Intent intent = new Intent();
        intent.setAction(ConnectivityManager.CONNECTIVITY_ACTION);

        connectivityReceiver.onReceive(context, intent);

        verify(merlinService).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void not_explode_when_the_merlin_service_is_null() throws Exception {
        Intent intent = new Intent();
        intent.setAction(ConnectivityManager.CONNECTIVITY_ACTION);
        merlinService = null;

        connectivityReceiver.onReceive(context, intent);
    }

}
