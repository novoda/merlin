package com.novoda.merlin.service;

import android.content.ContextWrapper;
import android.content.IntentFilter;
import android.os.Build;

public class TestDoubleMerlinServiceWithStubbedBuildersAndSdkVersionN extends TestDoubleMerlinServiceWithStubbedBuilders {

    private IntentFilter connectivityActionIntentFilter;

    TestDoubleMerlinServiceWithStubbedBuildersAndSdkVersionN(ContextWrapper context,
            NetworkStatusRetriever networkStatusRetriever,
            HostPinger defaultHostPinger,
            HostPinger customHostPinger) {
        super(context, networkStatusRetriever, defaultHostPinger, customHostPinger);
    }

    @Override
    protected boolean isSdkVersionLessThan(int version) {
        return Build.VERSION_CODES.N < version;
    }

    @Override
    protected IntentFilter getConnectivityActionIntentFilter() {
        if (null == connectivityActionIntentFilter) {
            connectivityActionIntentFilter = super.getConnectivityActionIntentFilter();
        }
        return connectivityActionIntentFilter;
    }
}
