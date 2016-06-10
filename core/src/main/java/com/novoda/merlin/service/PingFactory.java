package com.novoda.merlin.service;

class PingFactory {

    private final HostPinger.ResponseCodeFetcher fetcher;
    private final ResponseCodeValidator validator;

    public PingFactory(HostPinger.ResponseCodeFetcher fetcher, ResponseCodeValidator validator) {
        this.fetcher = fetcher;
        this.validator = validator;
    }

    public Ping create(String hostAddress) {
        return new Ping(hostAddress, fetcher, validator);
    }

}
