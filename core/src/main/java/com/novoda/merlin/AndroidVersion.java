package com.novoda.merlin;

import android.os.Build;

class AndroidVersion {

    private final int deviceVersion;

    AndroidVersion() {
        this(Build.VERSION.SDK_INT);
    }

    private AndroidVersion(int deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    boolean isLollipopOrHigher() {
        return deviceVersion >= Build.VERSION_CODES.LOLLIPOP;
    }
}
