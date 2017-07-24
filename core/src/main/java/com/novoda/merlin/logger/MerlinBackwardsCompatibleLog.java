package com.novoda.merlin.logger;

import android.util.Log;

public final class MerlinBackwardsCompatibleLog implements Logger.LogHandle {

    private static MerlinBackwardsCompatibleLog lazyInstance;

    private final String TAG = "Merlin";

    private MerlinBackwardsCompatibleLog() {
        // Single instance.
    }

    public static MerlinBackwardsCompatibleLog getInstance() {
        if (lazyInstance == null) {
            lazyInstance = new MerlinBackwardsCompatibleLog();
        }
        return lazyInstance;
    }

    @Override
    public void v(Object... message) {
        Log.v(TAG, message[0].toString());
    }

    @Override
    public void i(Object... message) {
        Log.i(TAG, message[0].toString());
    }

    @Override
    public void d(Object... msg) {
        Log.d(TAG, msg[0].toString());
    }

    @Override
    public void d(Throwable throwable, Object... message) {
        Log.d(TAG, message[0].toString(), throwable);
    }

    @Override
    public void w(Object... message) {
        Log.w(TAG, message[0].toString());
    }

    @Override
    public void w(Throwable throwable, Object... message) {
        Log.w(TAG, message[0].toString(), throwable);
    }

    @Override
    public void e(Object... message) {
        Log.e(TAG, message[0].toString());
    }

    @Override
    public void e(Throwable throwable, Object... message) {
        Log.e(TAG, message[0].toString(), throwable);
    }

}
