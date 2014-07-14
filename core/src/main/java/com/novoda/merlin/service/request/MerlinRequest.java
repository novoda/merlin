package com.novoda.merlin.service.request;

public class MerlinRequest implements Request {

    private final Request request;

    public static MerlinRequest head(String endpoint) {
        return head(new HttpRequestMaker(), endpoint);
    }

    public static MerlinRequest head(RequestMaker requestMaker, String endpoint) {
        return new MerlinRequest(requestMaker.head(endpoint));
    }

    MerlinRequest(Request request) {
        this.request = request;
    }

    @Override
    public int getResponseCode() {
        return request.getResponseCode();
    }

}
