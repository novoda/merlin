package com.novoda.merlin;

class EndpointPinger {

    private final Endpoint endpoint;
    private final PingTaskFactory pingTaskFactory;
    private final RequestMaker requestMaker;

    interface PingerCallback {

        void onSuccess();

        void onFailure();

    }

    static EndpointPinger withCustomRequestMakerEndpointAndValidation(RequestMaker requestMaker, Endpoint endpoint, ResponseCodeValidator validator) {
        PingTaskFactory pingTaskFactory = new PingTaskFactory(new ResponseCodeFetcher(), validator);
        return new EndpointPinger(requestMaker, endpoint, pingTaskFactory);
    }

    EndpointPinger(RequestMaker requestMaker, Endpoint endpoint, PingTaskFactory pingTaskFactory) {
        this.endpoint = endpoint;
        this.pingTaskFactory = pingTaskFactory;
        this.requestMaker = requestMaker;
    }

    void ping(PingerCallback pingerCallback) {
        PingTask pingTask = pingTaskFactory.create(requestMaker, endpoint, pingerCallback);
        pingTask.execute();
    }

    void noNetworkToPing(PingerCallback pingerCallback) {
        pingerCallback.onFailure();
    }

    static class ResponseCodeFetcher {

        public int from(RequestMaker requestMaker, Endpoint endpoint) {
            return MerlinRequest.head(requestMaker, endpoint).getResponseCode();
        }

    }

}
