package com.novoda.merlin.service;

import android.os.AsyncTask;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.service.request.RequestException;

class PingTask extends AsyncTask<Void, Void, Boolean> {

    private final Ping ping;
    private final HostPinger.PingerCallback pingerCallback;

    PingTask(Ping ping, HostPinger.PingerCallback pingerCallback) {
        this.ping = ping;
        this.pingerCallback = pingerCallback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return ping.doSynchronousPing();
        } catch (RequestException e) {
            MerlinLog.e("Ping task failed due to " + e.getMessage());
        }
        return false;
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
