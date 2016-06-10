package com.novoda.merlin.service;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.service.request.MerlinRequest;

class HostPinger {

    private final PingerCallback pingerCallback;
    private final PingFactory pingFactory;
    private final PingTaskFactory pingTaskFactory;
    private final String hostAddress;

    interface PingerCallback {

        void onSuccess();

        void onFailure();

    }

    public static HostPinger newInstance(PingerCallback pingerCallback) {
        MerlinLog.d("Host address not set, using Merlin default : " + Merlin.DEFAULT_ENDPOINT);
        PingFactory pingFactory = new PingFactory(new ResponseCodeFetcher(), ResponseCodeValidator.DEFAULT, RequestExceptionHandler.DEFAULT);
        return new HostPinger(pingerCallback, Merlin.DEFAULT_ENDPOINT, pingFactory, new PingTaskFactory(pingerCallback));
    }

    public static HostPinger newInstance(PingerCallback pingerCallback, String hostAddress) {
        PingFactory pingFactory = new PingFactory(new ResponseCodeFetcher(), ResponseCodeValidator.CUSTOM, RequestExceptionHandler.CUSTOM);
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
