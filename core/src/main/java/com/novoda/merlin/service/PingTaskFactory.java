package com.novoda.merlin.service;

class PingTaskFactory {

    private final HostPinger.PingerCallback pingerCallback;
    private final HostPinger.ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator responseCodeValidator;

    PingTaskFactory(HostPinger.PingerCallback pingerCallback, HostPinger.ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator responseCodeValidator) {
        this.pingerCallback = pingerCallback;
        this.responseCodeFetcher = responseCodeFetcher;
        this.responseCodeValidator = responseCodeValidator;
    }

    public PingTask create(String hostAddress) {
        Ping ping = new Ping(hostAddress, responseCodeFetcher, responseCodeValidator);
        return new PingTask(ping, pingerCallback);
    }

}
