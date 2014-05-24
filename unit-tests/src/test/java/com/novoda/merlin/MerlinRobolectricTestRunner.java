package com.novoda.merlin;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * A {@link org.robolectric.RobolectricTestRunner} implementation. Keeping this class for future configurations.
 */
public class MerlinRobolectricTestRunner extends RobolectricTestRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws org.junit.runners.model.InitializationError if junit says so
     */
    public MerlinRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
}
