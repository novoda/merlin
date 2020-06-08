package com.novoda.merlin;

class Ping {

    private final Endpoint endpoint;
    private final EndpointPinger.ResponseCodeFetcher responseCodeFetcher;
    private final ResponseCodeValidator validator;
    private final RequestMaker requestMaker;

    Ping(RequestMaker requestMaker, Endpoint endpoint, EndpointPinger.ResponseCodeFetcher responseCodeFetcher, ResponseCodeValidator validator) {
        this.endpoint = endpoint;
        this.responseCodeFetcher = responseCodeFetcher;
        this.validator = validator;
        this.requestMaker = requestMaker;
    }

    boolean doSynchronousPing() {
        Logger.d("Pinging: " + endpoint);
        try {
            return validator.isResponseCodeValid(responseCodeFetcher.from(requestMaker, endpoint));
        } catch (RequestException e) {
            if (!e.causedByIO()) {
                Logger.e("Ping task failed due to " + e.getMessage());
            }
            return false;
        }
    }

}
