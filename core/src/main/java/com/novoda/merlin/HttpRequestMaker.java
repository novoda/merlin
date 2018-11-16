package com.novoda.merlin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

class HttpRequestMaker implements RequestMaker {

    private static final String METHOD_HEAD = "HEAD";

    @Override
    public Request head(Endpoint endpoint) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = connectTo(endpoint);
            urlConnection.setRequestProperty("Accept-Encoding", "");

            setConnectionToHeadRequest(urlConnection);
            disableRedirects(urlConnection);

            return new MerlinHttpRequest(urlConnection);
        } catch (IOException e) {
            throw new RequestException(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private HttpURLConnection connectTo(Endpoint endpoint) throws IOException {
        URL url = endpoint.asURL();
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

        MerlinHttpRequest(HttpURLConnection request) {
            this.request = request;
        }

        @Override
        public int getResponseCode() {
            try {
                return request.getResponseCode();
            } catch (IOException e) {
                throw new RequestException(e);
            } finally {
                request.disconnect();
            }
        }
    }

}
