package com.novoda.merlin.service.request;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class HttpRequestMaker implements RequestMaker {

    /**
     * 'HEAD' request method
     */
    private final String METHOD_HEAD = "HEAD";

    @Override
    public Request head(String endpoint) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(METHOD_HEAD);
            urlConnection.setInstanceFollowRedirects(false);
            return new MerlinHttpRequest(urlConnection);
        }
        catch (MalformedURLException e) {
            throw new RequestException(e);
        } catch (IOException e) {
            throw new RequestException(e);
        }
    }

    private static class MerlinHttpRequest implements Request {

        private HttpURLConnection request;

        public MerlinHttpRequest(HttpURLConnection request) {
            this.request = request;
        }

        @Override
        public int getResponseCode(){
            try {
                return request.getResponseCode();
            } catch (IOException e) {
                throw new RequestException(e);
            }
        }

    }

}
