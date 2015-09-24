package com.novoda.merlin.service;

import android.os.AsyncTask;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.service.request.MerlinRequest;
import com.novoda.merlin.service.request.RequestException;

class HostPinger {

    private final PingerCallback pingerCallback;
    private final ResponseCodeFetcher responseCodeFetcher;

    private String hostAddress;

    interface PingerCallback {
        void onSuccess();

        void onFailure();
    }

    public HostPinger(PingerCallback pingerCallback) {
        this(pingerCallback, new ResponseCodeFetcher());
    }

    HostPinger(PingerCallback pingerCallback, ResponseCodeFetcher responseCodeFetcher) {
        this.pingerCallback = pingerCallback;
        this.responseCodeFetcher = responseCodeFetcher;
    }

    public static class ResponseCodeFetcher {
        public int from(String endpoint) {
            return MerlinRequest.head(endpoint).getResponseCode();
        }
    }

    public void setEndpoint(String endpoint) {
        this.hostAddress = endpoint;
    }

    public void ping() {
        PingTask pingTask = new PingTask(responseCodeFetcher, getHostAddress(), pingerCallback);
        pingTask.execute();
    }

    private String getHostAddress() {
        if (hostAddress == null) {
            MerlinLog.d("Host address has not been set, using Merlin default : " + Merlin.DEFAULT_ENDPOINT);
            return Merlin.DEFAULT_ENDPOINT;
        }
        return hostAddress;
    }

    public void noNetworkToPing() {
        pingerCallback.onFailure();
    }

    private static class PingTask extends AsyncTask<Void, Void, Boolean> {

        private static final int SUCCESS = 200;

        private final ResponseCodeFetcher responseCodeFetcher;
        private final String hostAddress;
        private final PingerCallback pingerCallback;

        PingTask(ResponseCodeFetcher responseCodeFetcher, String hostAddress, PingerCallback pingerCallback) {
            this.responseCodeFetcher = responseCodeFetcher;
            this.hostAddress = hostAddress;
            this.pingerCallback = pingerCallback;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                MerlinLog.d("Pinging : " + hostAddress);
                int responseCode = responseCodeFetcher.from(hostAddress);
                MerlinLog.d("Got response : " + responseCode);
                return responseCode == SUCCESS;
            } catch (RequestException e) {
                MerlinLog.e("Ping task failed due to " + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean pingResult) {
            super.onPostExecute(pingResult);
            if (pingResult) {
                pingerCallback.onSuccess();
            } else {
                pingerCallback.onFailure();
            }
        }

    }

}
