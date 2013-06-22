package com.novoda.robolectric;

import java.io.File;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.res.FsFile;
import org.robolectric.res.Fs;
import org.robolectric.AndroidManifest;

public class NovodaRobolectricTestRunner extends RobolectricTestRunner {

    public NovodaRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest createAppManifest(FsFile manifestFile) {
        FsFile androidManifest = Fs.newFile(getManifest());
        FsFile appBaseDir = androidManifest.getParent();
        return new AndroidManifest(manifestFile, appBaseDir.join("res"), appBaseDir.join("assets"));
    }

    private static File getManifest() {
        return new File(Thread.currentThread().getContextClassLoader().getResource("robolectric/AndroidManifest.xml").getPath());
    }

    private static File getResPath() {
        return new File(Thread.currentThread().getContextClassLoader().getResource("robolectric/res").getPath());
    }

}