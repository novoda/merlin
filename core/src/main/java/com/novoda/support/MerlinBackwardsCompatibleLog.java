package com.novoda.support;

public class MerlinBackwardsCompatibleLog implements Logger.LogHandle {

    private final String TAG = "Merlin" + hashCode();

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
