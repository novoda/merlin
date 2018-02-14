package com.novoda.merlin;

class PingTaskFactory {

    private final EndpointPinger.ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator responseCodeValidator;

    PingTaskFactory(EndpointPinger.ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator responseCodeValidator) {
        this.responseCodeFetcher = responseCodeFetcher;
        this.responseCodeValidator = responseCodeValidator;
    }

    PingTask create(Endpoint endpoint, EndpointPinger.PingerCallback pingerCallback) {
        Ping ping = new Ping(endpoint, responseCodeFetcher, responseCodeValidator);
        return new PingTask(ping, pingerCallback);
    }

}
