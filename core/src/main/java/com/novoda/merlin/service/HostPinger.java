package com.novoda.merlin.service;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.service.request.MerlinRequest;

import static com.novoda.merlin.service.RequestExceptionHandler.CustomEndpointRequestExceptionHandler;
import static com.novoda.merlin.service.RequestExceptionHandler.DefaultEndpointRequestExceptionHandler;
import static com.novoda.merlin.service.ResponseCodeValidator.CustomEndpointResponseCodeValidator;
import static com.novoda.merlin.service.ResponseCodeValidator.DefaultEndpointResponseCodeValidator;

class HostPinger {

    private final PingerCallback pingerCallback;
    private final String hostAddress;
    private final PingFactory pingFactory;
    private final PingTaskFactory pingTaskFactory;

    interface PingerCallback {

        void onSuccess();

        void onFailure();

    }

    public static HostPinger newInstance(PingerCallback pingerCallback) {
        MerlinLog.d("Host address not set, using Merlin default : " + Merlin.DEFAULT_ENDPOINT);
        PingFactory pingFactory = new PingFactory(
                new ResponseCodeFetcher(),
                new DefaultEndpointResponseCodeValidator(),
                new DefaultEndpointRequestExceptionHandler()
        );
        return new HostPinger(pingerCallback, Merlin.DEFAULT_ENDPOINT, pingFactory, new PingTaskFactory(pingerCallback));
    }

    public static HostPinger newInstance(PingerCallback pingerCallback, String hostAddress) {
        PingFactory pingFactory = new PingFactory(
                new ResponseCodeFetcher(),
                new CustomEndpointResponseCodeValidator(),
                new CustomEndpointRequestExceptionHandler()
        );
        return new HostPinger(pingerCallback, hostAddress, pingFactory, new PingTaskFactory(pingerCallback));
    }

    HostPinger(PingerCallback pingerCallback, String hostAddress, PingFactory pingFactory, PingTaskFactory pingTaskFactory) {
        this.pingerCallback = pingerCallback;
        this.hostAddress = hostAddress;
        this.pingFactory = pingFactory;
        this.pingTaskFactory = pingTaskFactory;
    }

    public void ping() {
        Ping ping = pingFactory.create(hostAddress);
        PingTask pingTask = pingTaskFactory.create(ping);
        pingTask.execute();
    }

    public void noNetworkToPing() {
        pingerCallback.onFailure();
    }

    public static class ResponseCodeFetcher {

        public int from(String endpoint) {
            return MerlinRequest.head(endpoint).getResponseCode();
        }

    }

}
