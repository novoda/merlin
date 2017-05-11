package com.novoda.merlin;

import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MerlinBuilderTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Context context;

    @Test
    public void whenBuildingMerlinInstanceWithLogging_thenLogsWithMerlinLog() {
        new MerlinBuilder()
                .withLogging(true)
                .build(context);

        assertTrue(MerlinLog.LOGGING);
    }

    @Test
    public void whenBuildingMerlinInstanceWithoutLogging_thenDoesNotLogWithMerlinLog() throws Exception {
        new MerlinBuilder()
                .withLogging(false)
                .build(context);

        assertFalse(MerlinLog.LOGGING);
    }

}
