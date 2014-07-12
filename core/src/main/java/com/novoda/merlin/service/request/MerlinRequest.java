package com.novoda.merlin.service.request;

public class MerlinRequest implements Request {

    private final Request request;

    public static MerlinRequest head(String hostname) {
        return head(new HttpRequestMaker(), hostname);
    }

    public static MerlinRequest head(RequestMaker requestMaker, String hostname) {
        return new MerlinRequest(requestMaker.head(hostname));
    }

    MerlinRequest(Request request) {
        this.request = request;
    }


    @Override
    public int getResponseCode() {
        return request.getResponseCode();
    }


}
