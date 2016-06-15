package com.novoda.merlin.service;

import android.content.ComponentName;
import android.content.pm.PackageManager;

class TestDoubleMerlinService extends MerlinService {

    private final PackageManager packageManager;
    private final ComponentName receiver;

    TestDoubleMerlinService(PackageManager packageManager, ComponentName receiver) {
        this.packageManager = packageManager;
        this.receiver = receiver;
    }

    @Override
    public PackageManager getPackageManager() {
        return packageManager;
    }

    @Override
    protected ComponentName connectivityReceiverComponent() {
        return receiver;
    }
}
