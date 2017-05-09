package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.test.mock.MockContext;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ConnectivityReceiverTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Intent intent;
    @Mock
    private MerlinService merlinService;

    private Context context;
    private ConnectivityReceiver connectivityReceiver;

    @Before
    public void setUp() throws Exception {
        context = new MockContext();
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
