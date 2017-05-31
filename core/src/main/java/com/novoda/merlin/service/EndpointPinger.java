package com.novoda.merlin.service;

import com.novoda.merlin.Endpoint;
import com.novoda.merlin.service.request.MerlinRequest;

class EndpointPinger {

    private final Endpoint endpoint;
    private final PingTaskFactory pingTaskFactory;

    interface PingerCallback {

        void onSuccess();

        void onFailure();

    }

    static EndpointPinger withCustomEndpointAndValidation(Endpoint endpoint, ResponseCodeValidator validator) {
        PingTaskFactory pingTaskFactory = new PingTaskFactory(new ResponseCodeFetcher(), validator);
        return new EndpointPinger(endpoint, pingTaskFactory);
    }

    EndpointPinger(Endpoint endpoint, PingTaskFactory pingTaskFactory) {
        this.endpoint = endpoint;
        this.pingTaskFactory = pingTaskFactory;
    }

    void ping(PingerCallback pingerCallback) {
        PingTask pingTask = pingTaskFactory.create(endpoint, pingerCallback);
        pingTask.execute();
    }

    void noNetworkToPing(PingerCallback pingerCallback) {
        pingerCallback.onFailure();
    }

    static class ResponseCodeFetcher {

        public int from(Endpoint endpoint) {
            return MerlinRequest.head(endpoint).getResponseCode();
        }

    }

}
