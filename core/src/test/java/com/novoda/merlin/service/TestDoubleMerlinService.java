package com.novoda.merlin.service;

import android.content.pm.PackageManager;

class TestDoubleMerlinService extends MerlinService {

    private final PackageManager packageManager;

    TestDoubleMerlinService(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public PackageManager getPackageManager() {
        return packageManager;
    }

}
