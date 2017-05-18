package com.novoda.merlin.service;

import com.novoda.merlin.Endpoint;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.service.request.MerlinRequest;
import com.novoda.support.Logger;

import static com.novoda.merlin.service.ResponseCodeValidator.DefaultEndpointResponseCodeValidator;

class HostPinger {

    private final PingerCallback pingerCallback;
    private final Endpoint endpoint;
    private final PingTaskFactory pingTaskFactory;

    interface PingerCallback {

        void onSuccess();

        void onFailure();

    }

    static HostPinger withDefaultEndpointValidation(PingerCallback pingerCallback) {
        Logger.d("Host address not set, using Merlin default: " + Merlin.DEFAULT_ENDPOINT);
        PingTaskFactory pingTaskFactory = new PingTaskFactory(pingerCallback, new ResponseCodeFetcher(), new DefaultEndpointResponseCodeValidator());
        return new HostPinger(pingerCallback, Endpoint.defaultEndpoint(), pingTaskFactory);
    }

    static HostPinger withCustomEndpointAndValidation(PingerCallback pingerCallback, Endpoint hostAddress, ResponseCodeValidator validator) {
        PingTaskFactory pingTaskFactory = new PingTaskFactory(pingerCallback, new ResponseCodeFetcher(), validator);
        return new HostPinger(pingerCallback, hostAddress, pingTaskFactory);
    }

    HostPinger(PingerCallback pingerCallback, Endpoint endpoint, PingTaskFactory pingTaskFactory) {
        this.pingerCallback = pingerCallback;
        this.endpoint = endpoint;
        this.pingTaskFactory = pingTaskFactory;
    }

    void ping() {
        PingTask pingTask = pingTaskFactory.create(endpoint);
        pingTask.execute();
    }

    void noNetworkToPing() {
        pingerCallback.onFailure();
    }

    static class ResponseCodeFetcher {

        public int from(Endpoint endpoint) {
            return MerlinRequest.head(endpoint).getResponseCode();
        }

    }

}
