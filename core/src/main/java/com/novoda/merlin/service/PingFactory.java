package com.novoda.merlin.service;

class PingFactory {

    private final HostPinger.ResponseCodeFetcher fetcher;
    private final ResponseCodeValidator validator;
    private final RequestExceptionHandler handler;

    public PingFactory(HostPinger.ResponseCodeFetcher fetcher, ResponseCodeValidator validator, RequestExceptionHandler handler) {
        this.fetcher = fetcher;
        this.validator = validator;
        this.handler = handler;
    }

    public Ping create(String hostAddress) {
        return new Ping(hostAddress, fetcher, validator, handler);
    }

}
