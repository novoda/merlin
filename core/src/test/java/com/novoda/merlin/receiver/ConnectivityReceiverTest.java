package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.test.mock.MockContext;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
public class ConnectivityReceiverTest {

    private ConnectivityReceiver connectivityReceiver;

    Context context;
    @Mock
    MerlinService merlinService;

    @Before
    public void setUp() throws Exception {
        context = new MockContext();
        initMocks(this);
        connectivityReceiver = new ConnectivityReceiver() {
            @Override
            protected MerlinService getMerlinService(Context context) {
                return merlinService;
            }

            @Override
            protected MerlinsBeard getMerlinsBeard(Context context) {
                MerlinsBeard mockMerlinsBeard = mock(MerlinsBeard.class);
                when(mockMerlinsBeard.isConnected()).thenReturn(true);
                return mockMerlinsBeard;
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
        Intent mockIntent = mock(Intent.class);
        when(mockIntent.getAction()).thenReturn("notNotifyTheMerlinServiceOnNonConnectivityIntents");

        connectivityReceiver.onReceive(context, mockIntent);

        verifyZeroInteractions(merlinService);
    }

    @Test
    public void notifyTheMerlinServiceOnValidConnectivityIntents() throws Exception {
        Intent mockIntent = mock(Intent.class);
        when(mockIntent.getAction()).thenReturn(ConnectivityManager.CONNECTIVITY_ACTION);

        connectivityReceiver.onReceive(context, mockIntent);

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
