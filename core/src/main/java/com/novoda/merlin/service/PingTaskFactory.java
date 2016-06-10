package com.novoda.merlin.service;

import static com.novoda.merlin.service.HostPinger.PingerCallback;
import static com.novoda.merlin.service.HostPinger.ResponseCodeFetcher;

class PingTaskFactory {

    private final PingerCallback pingerCallback;
    private final ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator responseCodeValidator;

    PingTaskFactory(PingerCallback pingerCallback, ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator responseCodeValidator) {
        this.pingerCallback = pingerCallback;
        this.responseCodeFetcher = responseCodeFetcher;
        this.responseCodeValidator = responseCodeValidator;
    }

    public PingTask create(String hostAddress) {
        Ping ping = new Ping(hostAddress, responseCodeFetcher, responseCodeValidator);
        return new PingTask(ping, pingerCallback);
    }

}
