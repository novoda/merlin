package com.novoda.merlin.receiver;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.novoda.merlin.service.AndroidVersion;
import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectivityChangesRegistrarShould {

    @Mock
    private Context context;
    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private AndroidVersion androidVersion;
    @Mock
    private MerlinService merlinService;

    private ConnectivityChangesRegistrar connectivityChangesRegistrar;

    @Before
    public void setUp() {
        initMocks(this);

        connectivityChangesRegistrar = new ConnectivityChangesRegistrar(context, connectivityManager, androidVersion, merlinService);
    }

    @Test
    public void givenRegisteredBroadcastReceiverWhenBindingForASecondTimeThenOriginalBroadcastReceieverIsRegisteredAgain() {
        ArgumentCaptor<ConnectivityReceiver> broadcastReceiver = givenRegisteredBroadcastReceiver();

        connectivityChangesRegistrar.register();

        verify(context, times(2)).registerReceiver(eq(broadcastReceiver.getValue()), Matchers.refEq(new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)));
    }

    @Test
    public void givenRegisteredBroadcastReceiverWhenUnbindingThenUnregistersOriginallyRegisteredBroadcastReceiver() {
        ArgumentCaptor<ConnectivityReceiver> broadcastReceiver = givenRegisteredBroadcastReceiver();

        connectivityChangesRegistrar.unregister();

        verify(context).unregisterReceiver(broadcastReceiver.getValue());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenRegisteredMerlinNetworkCallbacksWhenBindingForASecondTimeThenOriginalNetworkCallbacksIsRegisteredAgain() {
        ArgumentCaptor<MerlinNetworkCallbacks> merlinNetworkCallback = givenRegisteredMerlinNetworkCallbacks();

        connectivityChangesRegistrar.register();

        verify(connectivityManager, times(2)).registerNetworkCallback(Matchers.refEq((new NetworkRequest.Builder()).build()), eq(merlinNetworkCallback.getValue()));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenRegisteredMerlinNetworkCallbackWhenUnbindingThenUnregistersOriginallyRegisteredNetworkCallbacks() {
        ArgumentCaptor<MerlinNetworkCallbacks> merlinNetworkCallback = givenRegisteredMerlinNetworkCallbacks();

        connectivityChangesRegistrar.unregister();

        verify(connectivityManager).unregisterNetworkCallback(merlinNetworkCallback.getValue());
    }

    private ArgumentCaptor<ConnectivityReceiver> givenRegisteredBroadcastReceiver() {
        when(androidVersion.isLollipopOrHigher()).thenReturn(false);
        connectivityChangesRegistrar.register();
        ArgumentCaptor<ConnectivityReceiver> argumentCaptor = ArgumentCaptor.forClass(ConnectivityReceiver.class);
        verify(context).registerReceiver(argumentCaptor.capture(), Matchers.refEq(new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)));
        return argumentCaptor;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ArgumentCaptor<MerlinNetworkCallbacks> givenRegisteredMerlinNetworkCallbacks() {
        when(androidVersion.isLollipopOrHigher()).thenReturn(true);
        connectivityChangesRegistrar.register();
        ArgumentCaptor<MerlinNetworkCallbacks> argumentCaptor = ArgumentCaptor.forClass(MerlinNetworkCallbacks.class);
        verify(connectivityManager).registerNetworkCallback(Matchers.refEq((new NetworkRequest.Builder()).build()), argumentCaptor.capture());
        return argumentCaptor;
    }

}
