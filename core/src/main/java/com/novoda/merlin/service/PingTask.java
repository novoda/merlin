package com.novoda.merlin.service;

import android.os.AsyncTask;

class PingTask extends AsyncTask<Void, Void, Boolean> {

    private final Ping ping;
    private final EndpointPinger.PingerCallback pingerCallback;

    PingTask(Ping ping, EndpointPinger.PingerCallback pingerCallback) {
        this.ping = ping;
        this.pingerCallback = pingerCallback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return ping.doSynchronousPing();
    }

    @Override
    protected void onPostExecute(Boolean pingResultIsSuccess) {
        super.onPostExecute(pingResultIsSuccess);
        if (pingResultIsSuccess) {
            pingerCallback.onSuccess();
        } else {
            pingerCallback.onFailure();
        }
    }

}
