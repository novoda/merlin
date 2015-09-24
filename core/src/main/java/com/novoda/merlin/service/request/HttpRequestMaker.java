package com.novoda.merlin.service.request;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

class HttpRequestMaker implements RequestMaker {

    private static final String METHOD_HEAD = "HEAD";

    @Override
    public Request head(String endpoint) {
        try {
            HttpURLConnection urlConnection = connectTo(endpoint);
            urlConnection.setRequestProperty( "Accept-Encoding", "" );

            setConnectionToHeadRequest(urlConnection);
            disableRedirects(urlConnection);

            return new MerlinHttpRequest(urlConnection);
        } catch (MalformedURLException e) {
            throw new RequestException(e);
        } catch (IOException e) {
            throw new RequestException(e);
        }
    }

    private HttpURLConnection connectTo(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        return (HttpURLConnection) url.openConnection();
    }

    private void setConnectionToHeadRequest(HttpURLConnection urlConnection) throws ProtocolException {
        urlConnection.setRequestMethod(METHOD_HEAD);
    }

    private void disableRedirects(HttpURLConnection urlConnection) {
        urlConnection.setInstanceFollowRedirects(false);
    }

    private static class MerlinHttpRequest implements Request {

        private HttpURLConnection request;

        public MerlinHttpRequest(HttpURLConnection request) {
            this.request = request;
        }

        @Override
        public int getResponseCode() {
            try {
                return request.getResponseCode();
            } catch (IOException e) {
                throw new RequestException(e);
            }
        }

    }

}
