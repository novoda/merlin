package com.novoda.merlin.service.request;

import com.novoda.merlin.Endpoint;

public class MerlinRequest implements Request {

    private final Request request;

    public static MerlinRequest head(Endpoint endpoint) {
        return head(new HttpRequestMaker(), endpoint);
    }

    private static MerlinRequest head(RequestMaker requestMaker, Endpoint endpoint) {
        return new MerlinRequest(requestMaker.head(endpoint));
    }

    private MerlinRequest(Request request) {
        this.request = request;
    }

    @Override
    public int getResponseCode() {
        return request.getResponseCode();
    }

}
