package com.novoda.merlin.service;

import com.novoda.merlin.MerlinLog;

class Ping {

    private static final int OK = 200;
    private static final int NO_CONTENT = 204;

    private final String hostAddress;
    private final HostPinger.ResponseCodeFetcher responseCodeFetcher;

    Ping(String hostAddress, HostPinger.ResponseCodeFetcher responseCodeFetcher) {
        this.hostAddress = hostAddress;
        this.responseCodeFetcher = responseCodeFetcher;
    }

    public boolean doSynchronousPing() {
        MerlinLog.d("Pinging : " + hostAddress);
        int responseCode = responseCodeFetcher.from(hostAddress);
        MerlinLog.d("Got response : " + responseCode);
        return isSuccess(responseCode);
    }

    private static boolean isSuccess(int responseCode) {
        return responseCode == OK || responseCode == NO_CONTENT;
    }

}
