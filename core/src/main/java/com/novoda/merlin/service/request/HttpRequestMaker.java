package com.novoda.merlin.service.request;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.IOException;

class HttpRequestMaker implements RequestMaker {

    @Override
    public Request head(String endpoint) {
        return new MerlinHttpRequest(HttpRequest.head(endpoint));
    }

    private static class MerlinHttpRequest implements Request {

        private HttpRequest request;

        public MerlinHttpRequest(HttpRequest request) {
            this.request = request;
        }

        @Override
        public int getResponseCode() {
            try {
                return request.getConnection().getResponseCode();
            } catch (IOException e) {
                throw new RequestException(e);
            }
        }

    }

}
