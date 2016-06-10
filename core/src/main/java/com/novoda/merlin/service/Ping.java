package com.novoda.merlin.service;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.service.request.RequestException;

class Ping {

    private final String hostAddress;
    private final HostPinger.ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator validator;

    Ping(String hostAddress, HostPinger.ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator validator) {
        this.hostAddress = hostAddress;
        this.responseCodeFetcher = responseCodeFetcher;
        this.validator = validator;
    }

    public boolean doSynchronousPing() {
        MerlinLog.d("Pinging : " + hostAddress);
        try {
            return validator.isResponseCodeValid(responseCodeFetcher.from(hostAddress));
        } catch (RequestException e) {
            if (!e.causedByIO()) {
                MerlinLog.e("Ping task failed due to " + e.getMessage());
            }
            return false;
        }
    }

}
