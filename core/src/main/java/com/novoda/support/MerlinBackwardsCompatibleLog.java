package com.novoda.support;

public class MerlinBackwardsCompatibleLog implements Logger.LogHandle {

    private static final String TAG = "Merlin";
    private final boolean logging;

    public MerlinBackwardsCompatibleLog(boolean logging) {
        this.logging = logging;
    }

    @Override
    public void v(Object... message) {
        if (logging) {
            android.util.Log.v(TAG, message[0].toString());
        }
    }

    @Override
    public void i(Object... message) {
        if (logging) {
            android.util.Log.i(TAG, message[0].toString());
        }
    }

    @Override
    public void d(Object... msg) {
        if (logging) {
            android.util.Log.d(TAG, msg[0].toString());
        }
    }

    @Override
    public void d(Throwable throwable, Object... message) {
        if (logging) {
            android.util.Log.d(TAG, message[0].toString(), throwable);
        }
    }

    @Override
    public void w(Object... message) {
        if (logging) {
            android.util.Log.w(TAG, message[0].toString());
        }
    }

    @Override
    public void w(Throwable throwable, Object... message) {
        if (logging) {
            android.util.Log.w(TAG, message[0].toString(), throwable);
        }
    }

    @Override
    public void e(Object... message) {
        if (logging) {
            android.util.Log.e(TAG, message[0].toString());
        }
    }

    @Override
    public void e(Throwable throwable, Object... message) {
        if (logging) {
            android.util.Log.e(TAG, message[0].toString(), throwable);
        }
    }

}
