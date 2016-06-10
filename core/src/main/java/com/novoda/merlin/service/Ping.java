package com.novoda.merlin.service;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.service.request.RequestException;

class Ping {

    private final String hostAddress;
    private final HostPinger.ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator validator;
    private final RequestExceptionHandler handler;

    Ping(String hostAddress, HostPinger.ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator validator, RequestExceptionHandler handler) {
        this.hostAddress = hostAddress;
        this.responseCodeFetcher = responseCodeFetcher;
        this.validator = validator;
        this.handler = handler;
    }

    public boolean doSynchronousPing() throws RequestException {
        MerlinLog.d("Pinging : " + hostAddress);
        try {
            return validator.isResponseCodeValid(responseCodeFetcher.from(hostAddress));
        } catch (RequestException e) {
            return handler.handleRequestException(e);
        }
    }

}
