package com.novoda.merlin.service;

import com.novoda.merlin.MerlinLog;

class Ping {

    private static final int SUCCESS = 2;
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
        int startingNumber = (responseCode / 100);
        return startingNumber == SUCCESS;
    }

}
