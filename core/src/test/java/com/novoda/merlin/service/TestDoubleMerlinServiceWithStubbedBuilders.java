package com.novoda.merlin.service;

import android.content.ContextWrapper;

class TestDoubleMerlinServiceWithStubbedBuilders extends MerlinService {

    private final ContextWrapper context;
    private final NetworkStatusRetriever networkStatusRetriever;
    private final HostPinger defaultHostPinger;
    private final HostPinger customHostPinger;

    TestDoubleMerlinServiceWithStubbedBuilders(
            ContextWrapper context,
            NetworkStatusRetriever networkStatusRetriever,
            HostPinger defaultHostPinger,
            HostPinger customHostPinger
    ) {
        this.context = context;
        this.networkStatusRetriever = networkStatusRetriever;
        this.defaultHostPinger = defaultHostPinger;
        this.customHostPinger = customHostPinger;
    }

    @Override
    public ContextWrapper getApplicationContext() {
        return context;
    }

    @Override
    protected NetworkStatusRetriever buildNetworkStatusRetriever() {
        return networkStatusRetriever;
    }

    @Override
    protected HostPinger buildDefaultHostPinger() {
        return defaultHostPinger;
    }

    @Override
    protected HostPinger buildHostPinger(String hostName, ResponseCodeValidator validator) {
        return customHostPinger;
    }
}
