package com.novoda.merlin;

class PingTaskFactory {

    private final EndpointPinger.ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator responseCodeValidator;

    PingTaskFactory(EndpointPinger.ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator responseCodeValidator) {
        this.responseCodeFetcher = responseCodeFetcher;
        this.responseCodeValidator = responseCodeValidator;
    }

    PingTask create(RequestMaker requestMaker, Endpoint endpoint, EndpointPinger.PingerCallback pingerCallback) {
        Ping ping = new Ping(requestMaker, endpoint, responseCodeFetcher, responseCodeValidator);
        return new PingTask(ping, pingerCallback);
    }

}
