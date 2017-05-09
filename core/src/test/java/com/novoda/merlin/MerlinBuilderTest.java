package com.novoda.merlin;

import android.test.mock.MockApplication;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MerlinBuilderTest {

    @Test
    public void buildInstanceWithLoggingEnabled() throws Exception {
        Merlin merlin = new MerlinBuilder()
                .withLogging(true)
                .build(new MockApplication());

        assertTrue(MerlinLog.LOGGING);
    }

    @Test
    public void buildInstanceWithLoggingDisabled() throws Exception {
        Merlin merlin = new MerlinBuilder()
                .withLogging(false)
                .build(new MockApplication());

        assertFalse(MerlinLog.LOGGING);
    }

}
