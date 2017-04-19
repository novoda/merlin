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
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CompatibilityLayerShould {

    @Mock
    private Context context;
    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private AndroidVersion androidVersion;
    @Mock
    private MerlinService merlinService;

    private CompatibilityLayer compatibilityLayer;

    @Before
    public void setUp() {
        initMocks(this);

        compatibilityLayer = new CompatibilityLayer(context, connectivityManager, androidVersion, merlinService);
    }

    @Test
    public void registerBroadcastReceiverWhenAndroidVersionIsBelowLollipop() {
        when(androidVersion.isLollipopOrHigher()).thenReturn(false);

        compatibilityLayer.bind();

        verify(context).registerReceiver(any(ConnectivityReceiver.class), any(IntentFilter.class));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void registerMerlinNetworkCallbackWhenAndroidVersionIsLollipopOrAbove() {
        when(androidVersion.isLollipopOrHigher()).thenReturn(true);

        compatibilityLayer.unbind();

        verify(connectivityManager).registerNetworkCallback(any(NetworkRequest.class), any(MerlinNetworkCallbacks.class));
    }

    @Test
    public void unregisterBroadcastReceiverWhenAndroidVersionIsBelowLollipop() {
        when(androidVersion.isLollipopOrHigher()).thenReturn(false);

        compatibilityLayer.unbind();

        verify(context).unregisterReceiver(any(ConnectivityReceiver.class));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void unregisterMerlinNetworkCallbackWhenAndroidVersionIsLollipopOrAbove() {
        when(androidVersion.isLollipopOrHigher()).thenReturn(true);

        compatibilityLayer.unbind();

        verify(connectivityManager).unregisterNetworkCallback(any(MerlinNetworkCallbacks.class));
    }

}
