package com.novoda.merlin.service;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.service.request.RequestException;

class Ping {

    private final String hostAddress;
    private final HostPinger.ResponseCodeFetcher responseCodeFetcher;

    Ping(String hostAddress, HostPinger.ResponseCodeFetcher responseCodeFetcher) {
        this.hostAddress = hostAddress;
        this.responseCodeFetcher = responseCodeFetcher;
    }

    public boolean doSynchronousPing() {
        MerlinLog.d("Pinging : " + hostAddress);
        try {
            responseCodeFetcher.from(hostAddress);
        } catch (RequestException e) {
            if (e.causedByIO()) {
                return false;
            }

            throw e;
        }
        return true;
    }

}
