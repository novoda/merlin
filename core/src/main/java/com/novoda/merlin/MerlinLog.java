package com.novoda.merlin;

public class MerlinLog {

    static boolean LOGGING = false;
    private static final String TAG = "Merlin";

    public static void v(String message) {
        if (LOGGING) {
            android.util.Log.v(TAG, message);
        }
    }

    public static void d(String message) {
        if (LOGGING) {
            android.util.Log.d(TAG, message);
        }
    }

    public static void e(String message) {
        if (LOGGING) {
            android.util.Log.e(TAG, message);
        }
    }

}
