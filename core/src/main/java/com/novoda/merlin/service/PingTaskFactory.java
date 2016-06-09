package com.novoda.merlin.service;

class PingTaskFactory {

    private final HostPinger.PingerCallback pingerCallback;
    private final HostPinger.ResponseCodeFetcher responseCodeFetcher;

    PingTaskFactory(HostPinger.PingerCallback pingerCallback, HostPinger.ResponseCodeFetcher responseCodeFetcher) {
        this.pingerCallback = pingerCallback;
        this.responseCodeFetcher = responseCodeFetcher;
    }

    public PingTask create(String hostAddress) {
        Ping ping = new Ping(hostAddress, responseCodeFetcher, ResponseCodeValidator.CUSTOM, RequestExceptionHandler.CUSTOM);
        return new PingTask(ping, pingerCallback);
    }

}
