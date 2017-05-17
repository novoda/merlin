package com.novoda.support;

public final class MerlinBackwardsCompatibleLog implements Logger.LogHandle {

    private static MerlinBackwardsCompatibleLog lazyInstance;

    private final String TAG = "Merlin" + hashCode();

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
        android.util.Log.v(TAG, message[0].toString());
    }

    @Override
    public void i(Object... message) {
        android.util.Log.i(TAG, message[0].toString());
    }

    @Override
    public void d(Object... msg) {
        android.util.Log.d(TAG, msg[0].toString());
    }

    @Override
    public void d(Throwable throwable, Object... message) {
        android.util.Log.d(TAG, message[0].toString(), throwable);
    }

    @Override
    public void w(Object... message) {
        android.util.Log.w(TAG, message[0].toString());
    }

    @Override
    public void w(Throwable throwable, Object... message) {
        android.util.Log.w(TAG, message[0].toString(), throwable);
    }

    @Override
    public void e(Object... message) {
        android.util.Log.e(TAG, message[0].toString());
    }

    @Override
    public void e(Throwable throwable, Object... message) {
        android.util.Log.e(TAG, message[0].toString(), throwable);
    }

}
