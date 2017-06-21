package com.novoda.merlin.service;

import android.os.Build;

public class AndroidVersion {

    private final int deviceVersion;

    public AndroidVersion() {
        this(Build.VERSION.SDK_INT);
    }

    private AndroidVersion(int deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public boolean isLollipopOrHigher() {
        return deviceVersion >= Build.VERSION_CODES.LOLLIPOP;
    }
}
