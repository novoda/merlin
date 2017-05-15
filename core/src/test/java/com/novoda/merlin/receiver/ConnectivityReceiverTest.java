package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ConnectivityReceiverTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Context context;
    @Mock
    private Intent intent;
    @Mock
    private MerlinService merlinService;
    @Mock
    private MerlinsBeard merlinsBeard;
    @Mock
    private ConnectivityChangeEventCreator connectivityChangeEventCreator;
    @Mock
    private ConnectivityChangeEvent connectivityChangeEvent;

    private ConnectivityReceiver connectivityReceiver;

    @Before
    public void setUp() throws Exception {
        connectivityReceiver = new ConnectivityReceiver(new ConnectivityReceiver.MerlinsBeardRetriever() {
            @Override
            public MerlinsBeard getMerlinsBeard(Context context1) {
                return merlinsBeard;
            }
        }, new ConnectivityReceiver.ServiceRetriever() {
            @Override
            public MerlinService getService(Context context1) {
                return merlinService;
            }
        }, connectivityChangeEventCreator);

        given(connectivityChangeEventCreator.createFrom(any(Intent.class), any(MerlinsBeard.class))).willReturn(connectivityChangeEvent);
    }

    @Test
    public void givenNullIntent_whenReceivingAnIntent_thenDoesNotNotifyMerlinService() {
        connectivityReceiver.onReceive(context, null);

        verifyZeroInteractions(merlinService);
    }

    @Test
    public void givenNoConnectivityIntents_whenReceivingAnIntent_thenDoesNotNotifyMerlinService() {
        given(intent.getAction()).willReturn("notNotifyTheMerlinServiceOnNonConnectivityIntents");

        connectivityReceiver.onReceive(context, intent);

        verifyZeroInteractions(merlinService);
    }

    @Test
    public void givenValidConnectivityIntent_whenReceivingAnIntent_thenNotifiesMerlinService() {
        given(intent.getAction()).willReturn(ConnectivityManager.CONNECTIVITY_ACTION);

        connectivityReceiver.onReceive(context, intent);

        verify(merlinService).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenNullMerlinService_whenReceivingAnIntent_thenDoesNotThrowAnException() {
        given(intent.getAction()).willReturn(ConnectivityManager.CONNECTIVITY_ACTION);
        merlinService = null;

        connectivityReceiver.onReceive(context, intent);
    }

}
