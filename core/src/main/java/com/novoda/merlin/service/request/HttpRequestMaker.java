package com.novoda.merlin.service.request;

import com.github.kevinsawicki.http.HttpRequest;

class HttpRequestMaker implements RequestMaker {

    @Override
    public Request head(String hostname) {
        return new MerlinHttpRequest(HttpRequest.head(hostname));
    }

    private static class MerlinHttpRequest implements Request {

        private HttpRequest request;

        public MerlinHttpRequest(HttpRequest request) {
            this.request = request;
        }

        @Override
        public int getResponseCode() {
            try {
                return request.followRedirects(false).code();
            } catch (HttpRequest.HttpRequestException e) {
                throw new RequestException(e);
            }
        }

    }

}
