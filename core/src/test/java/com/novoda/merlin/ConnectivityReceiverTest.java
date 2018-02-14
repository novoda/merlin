package com.novoda.merlin;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectivityReceiverTest {

    private final Context context = mock(Context.class);
    private final Intent intent = mock(Intent.class);
    private final ConnectivityReceiverConnectivityChangeNotifier notifier = mock(ConnectivityReceiverConnectivityChangeNotifier.class);

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
