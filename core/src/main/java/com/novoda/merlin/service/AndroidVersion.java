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

    public boolean isMarshmallowOrHigher() {
        return deviceVersion >= Build.VERSION_CODES.M;
    }

    public boolean isNougatOrHigher() {
        return deviceVersion >= Build.VERSION_CODES.N;
    }
}
