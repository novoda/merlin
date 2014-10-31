package com.novoda.merlin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MerlinRobolectricTestRunner.class)
public class MerlinBuilderShould {

    @Test
    public void buildInstanceWithLoggingEnabled() throws Exception {
        Merlin merlin = new MerlinBuilder()
                .withLogging(true)
                .build(Robolectric.application);

        assertTrue(MerlinLog.LOGGING);
    }

    @Test
    public void buildInstanceWithLoggingDisabled() throws Exception {
        Merlin merlin = new MerlinBuilder()
                .withLogging(false)
                .build(Robolectric.application);

        assertFalse(MerlinLog.LOGGING);
    }

}
