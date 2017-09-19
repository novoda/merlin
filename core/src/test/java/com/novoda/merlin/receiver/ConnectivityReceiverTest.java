package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

public class ConnectivityReceiverTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Context context;
    @Mock
    private Intent intent;
    @Mock
    private ConnectivityReceiverConnectivityChangeNotifier notifier;

    private ConnectivityReceiver connectivityReceiver;

    @Before
    public void setUp() {
        connectivityReceiver = new ConnectivityReceiver(notifier);
    }

    @Test
    public void whenReceivingAnIntent_thenForwardsToConnectivityChangeNotifier() {
        connectivityReceiver.onReceive(context, intent);

        verify(notifier).notify(context, intent);
    }

}
