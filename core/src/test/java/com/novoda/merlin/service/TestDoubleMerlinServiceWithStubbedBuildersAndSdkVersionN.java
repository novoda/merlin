package com.novoda.merlin.service;

import android.content.ContextWrapper;
import android.content.IntentFilter;

public class TestDoubleMerlinServiceWithStubbedBuildersAndSdkVersionN extends TestDoubleMerlinServiceWithStubbedBuilders {

    private IntentFilter connectivityActionIntentFilter;

    TestDoubleMerlinServiceWithStubbedBuildersAndSdkVersionN(ContextWrapper context,
            NetworkStatusRetriever networkStatusRetriever,
            HostPinger defaultHostPinger,
            HostPinger customHostPinger) {
        super(context, networkStatusRetriever, defaultHostPinger, customHostPinger);
    }

    @Override
    protected IntentFilter getConnectivityActionIntentFilter() {
        if (null == connectivityActionIntentFilter) {
            connectivityActionIntentFilter = super.getConnectivityActionIntentFilter();
        }
        return connectivityActionIntentFilter;
    }
}
