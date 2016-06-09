package com.novoda.merlin.service;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.service.request.MerlinRequest;
import com.novoda.merlin.service.request.RequestException;

class HostPinger {

    private final PingerCallback pingerCallback;
    private final String hostAddress;
    private final PingTaskFactory pingTaskFactory;

    interface PingerCallback {

        void onSuccess();

        void onFailure();

    }

    interface ResponseCodeValidator {
        boolean isResponseCodeValid(int responseCode);
        boolean isResponseCodeValid(RequestException e);

        ResponseCodeValidator DEFAULT_ENDPOINT_VALIDATOR = new ResponseCodeValidator() {
            @Override
            public boolean isResponseCodeValid(int responseCode) {
                return responseCode == 204;
            }

            @Override
            public boolean isResponseCodeValid(RequestException e) {
                return false;
            }
        };

        ResponseCodeValidator CUSTOM_ENDPOINT_VALIDATOR = new ResponseCodeValidator() {
            @Override
            public boolean isResponseCodeValid(int responseCode) {
                return true;
            }

            @Override
            public boolean isResponseCodeValid(RequestException e) {
                if (e.causedByIO()) {
                    return false;
                }
                throw e;
            }
        };
    }

    public static HostPinger newInstance(PingerCallback pingerCallback) {
        MerlinLog.d("Host address not set, using Merlin default : " + Merlin.DEFAULT_ENDPOINT);
        return newInstance(pingerCallback, Merlin.DEFAULT_ENDPOINT);
    }

    public static HostPinger newInstance(PingerCallback pingerCallback, String hostAddress) {
        ResponseCodeFetcher responseCodeFetcher = new ResponseCodeFetcher();
        PingTaskFactory pingTaskFactory = new PingTaskFactory(pingerCallback, responseCodeFetcher);
        return new HostPinger(pingerCallback, hostAddress, pingTaskFactory);
    }

    HostPinger(PingerCallback pingerCallback, String hostAddress, PingTaskFactory pingTaskFactory) {
        this.pingerCallback = pingerCallback;
        this.hostAddress = hostAddress;
        this.pingTaskFactory = pingTaskFactory;
    }

    public void ping() {
        PingTask pingTask = pingTaskFactory.create(hostAddress);
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
