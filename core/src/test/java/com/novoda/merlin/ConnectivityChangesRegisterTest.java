package com.novoda.merlin;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.*;

public class ConnectivityChangesRegisterTest {

    private final Context context = mock(Context.class);
    private final ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
    private final AndroidVersion androidVersion = mock(AndroidVersion.class);
    private final MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier = mock(MerlinService.ConnectivityChangesNotifier.class);
    private final ConnectivityChangeEventExtractor extractor = mock(ConnectivityChangeEventExtractor.class);

    private ConnectivityChangesRegister connectivityChangesRegister;

    @Before
    public void setUp() {
        connectivityChangesRegister = new ConnectivityChangesRegister(context, connectivityManager, androidVersion, extractor);
    }

    @Test
    public void givenRegisteredBroadcastReceiver_whenBindingForASecondTime_thenOriginalBroadcastReceiverIsRegisteredAgain() {
        ArgumentCaptor<ConnectivityReceiver> broadcastReceiver = givenRegisteredBroadcastReceiver();

        connectivityChangesRegister.register(connectivityChangesNotifier);

        verify(context, times(2)).registerReceiver(eq(broadcastReceiver.getValue()), refEq(new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)));
    }

    @Test
    public void givenRegisteredBroadcastReceiver_whenUnbinding_thenUnregistersOriginallyRegisteredBroadcastReceiver() {
        ArgumentCaptor<ConnectivityReceiver> broadcastReceiver = givenRegisteredBroadcastReceiver();

        connectivityChangesRegister.unregister();

        verify(context).unregisterReceiver(broadcastReceiver.getValue());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenRegisteredMerlinNetworkCallbacks_whenBindingForASecondTime_thenOriginalNetworkCallbacksIsRegisteredAgain() {
        ArgumentCaptor<ConnectivityCallbacks> merlinNetworkCallback = givenRegisteredMerlinNetworkCallbacks();

        connectivityChangesRegister.register(connectivityChangesNotifier);

        verify(connectivityManager, times(2)).registerNetworkCallback(refEq((new NetworkRequest.Builder()).build()), eq(merlinNetworkCallback.getValue()));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenRegisteredMerlinNetworkCallback_whenUnbinding_thenUnregistersOriginallyRegisteredNetworkCallbacks() {
        ArgumentCaptor<ConnectivityCallbacks> merlinNetworkCallback = givenRegisteredMerlinNetworkCallbacks();

        connectivityChangesRegister.unregister();

        verify(connectivityManager).unregisterNetworkCallback(merlinNetworkCallback.getValue());
    }

    private ArgumentCaptor<ConnectivityReceiver> givenRegisteredBroadcastReceiver() {
        given(androidVersion.isLollipopOrHigher()).willReturn(false);
        connectivityChangesRegister.register(connectivityChangesNotifier);
        ArgumentCaptor<ConnectivityReceiver> argumentCaptor = ArgumentCaptor.forClass(ConnectivityReceiver.class);
        verify(context).registerReceiver(argumentCaptor.capture(), refEq(new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)));
        return argumentCaptor;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ArgumentCaptor<ConnectivityCallbacks> givenRegisteredMerlinNetworkCallbacks() {
        given(androidVersion.isLollipopOrHigher()).willReturn(true);
        connectivityChangesRegister.register(connectivityChangesNotifier);
        ArgumentCaptor<ConnectivityCallbacks> argumentCaptor = ArgumentCaptor.forClass(ConnectivityCallbacks.class);
        verify(connectivityManager).registerNetworkCallback(refEq((new NetworkRequest.Builder()).build()), argumentCaptor.capture());
        return argumentCaptor;
    }

}
