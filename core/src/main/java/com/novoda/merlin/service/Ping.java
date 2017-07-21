package com.novoda.merlin.service;

import com.novoda.merlin.Endpoint;
import com.novoda.merlin.service.request.RequestException;
import com.novoda.merlin.logger.Logger;

class Ping {

    private final Endpoint endpoint;
    private final EndpointPinger.ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator validator;

    Ping(Endpoint endpoint, EndpointPinger.ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator validator) {
        this.endpoint = endpoint;
        this.responseCodeFetcher = responseCodeFetcher;
        this.validator = validator;
    }

    boolean doSynchronousPing() {
        Logger.d("Pinging: " + endpoint);
        try {
            return validator.isResponseCodeValid(responseCodeFetcher.from(endpoint));
        } catch (RequestException e) {
            if (!e.causedByIO()) {
                Logger.e("Ping task failed due to " + e.getMessage());
            }
            return false;
        }
    }

}
