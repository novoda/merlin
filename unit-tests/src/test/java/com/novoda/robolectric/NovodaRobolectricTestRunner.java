package com.novoda.robolectric;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

public class NovodaRobolectricTestRunner extends RobolectricTestRunner {

    public NovodaRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

}