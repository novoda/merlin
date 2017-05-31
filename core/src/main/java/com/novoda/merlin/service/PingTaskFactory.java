package com.novoda.merlin.service;

import com.novoda.merlin.Endpoint;

import static com.novoda.merlin.service.EndpointPinger.PingerCallback;
import static com.novoda.merlin.service.EndpointPinger.ResponseCodeFetcher;

class PingTaskFactory {

    private final ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator responseCodeValidator;

    PingTaskFactory(ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator responseCodeValidator) {
        this.responseCodeFetcher = responseCodeFetcher;
        this.responseCodeValidator = responseCodeValidator;
    }

    PingTask create(Endpoint endpoint, PingerCallback pingerCallback) {
        Ping ping = new Ping(endpoint, responseCodeFetcher, responseCodeValidator);
        return new PingTask(ping, pingerCallback);
    }

}
